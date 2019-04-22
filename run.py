#!/usr/bin/env python3

import os, datetime, json
from flask import Flask, request, session, flash, jsonify, make_response
from flask_bcrypt import Bcrypt
from flask_login import LoginManager, login_user, logout_user, login_required, login_fresh, confirm_login, fresh_login_required
import pyotp
from mongoengine import *
from mongoengine.context_managers import switch_collection
from werkzeug.exceptions import BadRequest, Unauthorized

app = Flask(__name__, static_url_path='', static_folder='web/dist/keyper')
lm = LoginManager()
lm.init_app(app)
bcrypt = Bcrypt(app)

SECRET_KEY = os.environ.get("SECRET_KEY")
if not SECRET_KEY:
	raise ValueError("No secret key set in OS for API")

app.config['SECRET_KEY'] = SECRET_KEY

#TOTP_SECRET = pyotp.random_base32()
TOTP_SECRET = os.environ.get("TOTP_SECRET")
if not TOTP_SECRET:
	raise ValueError("No secret key for TOTP in OS for API")

skip_TOTP = False

# db = keyper, mongodb://172.0.0.1:27017
#connect('keyper')
# test database below. only need line above in prod.
connect('keyper', host='mongodb://test:testUser1@ds161104.mlab.com:61104/practice')

# Encrypted site specific password blobs
class SiteInfo(EmbeddedDocument):
	id = StringField()
	content = BinaryField()

class User(Document):
	username = StringField(max_length=64, required=True)
	email = EmailField(required=True)
	password = StringField(required=True)
	secretKey = BinaryField()
	sites = ListField(EmbeddedDocumentField(SiteInfo))

	# Necessary properties for User class to work with flask_login
	# Default to not authed or active
	def is_authenticated(self):
		return False

	def is_active(self):
		return False

	def is_anonymous(self):
		return False

	def get_id(self):
		return self.username

@lm.user_loader
def load_user(username):
	with switch_collection(User, 'users') as toGet:
		user = User.objects.get(username__exact = username)
		if not user:
			return None

		return user

@app.route('/')
@app.route('/home')
@app.route('/login')
@app.route('/signup')
@app.route('/2fa')
def basic_pages(**kwargs):
    return make_response(open('web/dist/keyper/index.html').read())

@app.route('/api/register', methods=['GET','POST'])
def insert():
	error = None
	# Generate password hash, 12 rounds
	pwHash = bcrypt.generate_password_hash(request.form['password'])
	newUser = User(username=request.form['username'], email=request.form['email'], password=pwHash)
	with switch_collection(User, 'users') as toGet:
		try:
			if User.objects.get(username__exact = str(request.form['username'])):
				raise BadRequest('Registration Error, please try again.')

		except DoesNotExist:
			with switch_collection(User, 'users') as toAdd:
        newUser.secretKey = pyotp.random_base32();
				newUser.save(validate=True)
        totp = pyotp.TOTP(newUser.secretKey)
				uri = totp.provisioning_uri(request.form['email'], issuer_name ='Kepyer.pro')
				session['verify'] = newUser.username
				# totp uri, can be used to generate QR code
				return uri

@app.route('/api/delete', methods=['GET', 'POST'])
@login_required
def delete():
	if login_fresh() == True:
		try:
			error = None
			user = session['user_id']
			with switch_collection(User, 'users') as toDel:
				User.objects(username=user).delete()
				logout_user()
				return 'User %s has been deleted' %user

		except DoesNotExist:
				raise BadRequest('User does not exist.')

	else:
		raise Unauthorized('Session is not fresh.')

@app.route('/api/login', methods=['GET', 'POST'])
def login():
	user = request.form['username']
	password = request.form['password']
	with switch_collection(User, 'users') as toGet:
		try:
			search = User.objects.get(username__exact = user)
			if bcrypt.check_password_hash(search.password, password):
				if skip_TOTP == True:
					login_user(search)
					return 'Success'
				else:
					session['verify'] = user
					return jsonify(success=True)

			else:
				raise Unauthorized('Registration error. Try again.')

		except DoesNotExist:
			raise BadRequest('Registration Error. Try again.')

@app.route('/api/token', methods=['GET', 'POST'])
def verifyToken():
	if not session['verify']:
		raise Unauthorized('Please verify username and password.')

	else:
		username = session['verify']
		with switch_collection(User, 'users') as toGet:
			try:
				user = User.objects.get(username__exact = username)

				totp = pyotp.TOTP(user.secretKey if user.secretKey is not None else TOTP_SECRET)
				if not totp.verify(request.form['token']):
					raise Unauthorized('Incorrect auth code')

				login_user(user, remember=True, force=True, duration=datetime.timedelta(days=14))
				session.pop('verify', None)
				return 'True'

			except DoesNotExist:
				raise BadRequest('Error has occurred. Try again.')


@app.route('/api/sites', methods=['GET'])
@login_required
def returnSites():
	username = session['user_id']
	with switch_collection(User, 'users') as toGet:
		userObj = User.objects.get(username__exact = username)
		return jsonify(list(map(lambda site: {"content" : site.content.hex(), "id" : site.id}, userObj.sites)))

@app.route('/api/site/<id>', methods=['POST'])
@login_required
def addsites(id):
	with switch_collection(User, 'users') as toAdd:
		user = User.objects.get(username__exact = session['user_id'])
		info = SiteInfo(id = id, content = request.get_data())
		updated = User.objects(id = user.id, sites__id = id).update(set__sites__S__content = info.content)
		if not updated:
			User.objects(id = user.id).update_one(push__sites = info)

		user.save(validate = True)
		return jsonify({ "success": "updated" if updated else "new" })

@app.route('/api/site/<id>', methods=['DELETE'])
@login_required
def deleteSites(id):
	with switch_collection(User, 'users') as toDel:
		try:
			user = User.objects.get(username__exact = session['user_id'])

			try:
				removed = User.objects(id = user.id).get(sites__id = id)
				User.objects(id = user.id).update_one(pull__sites = {'id' : id})
				user.save(validate = True)
				return jsonify({ 'success': id });

			except DoesNotExist:
				raise BadRequest('Site info does not exist.')

		except DoesNotExist:
			raise BadRequest('User not found.')

@app.route('/api/logout', methods=['GET', 'POST'])
@login_required
def logout():
	logout_user()
	return "Logged out successfully."

if __name__ == '__main__':
    app.run()
