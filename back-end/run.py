#!/usr/bin/python3

import re, os, datetime
import json
from flask import Flask, request, session, flash
from flask_bcrypt import Bcrypt
from flask_login import LoginManager, login_user, logout_user, login_required, login_fresh, confirm_login, fresh_login_required
import pyotp
from mongoengine import *
from mongoengine.context_managers import switch_collection

app = Flask(__name__)
lm = LoginManager()
lm.init_app(app)
bcrypt = Bcrypt(app)

SECRET_KEY = os.environ.get("SECRET_KEY")
if not SECRET_KEY:
	raise ValueError("No secret key set in OS for API")

app.config['SECRET_KEY'] = SECRET_KEY

# db = keyper, mongodb://172.0.0.1:27017
#test database below. Eventually replace with real database
connect('keyper')
#connect('keyper', host='mongodb://test:testUser1@ds161104.mlab.com:61104/practice')

# Encrypted site specific password blobs
class siteInfo(EmbeddedDocument):
	content = BinaryField()

class User(Document):
	username = StringField(max_length=64, required=True)
	email = StringField(max_length=64, required=True)
	password = StringField(required=True)
	sites = ListField(EmbeddedDocumentField(siteInfo))

	# Necessary properties for User class to work with flask_login
	def is_authenticated(self):
		return True

	def is_active(self):
		return True

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
def hello_world():
    return 'Hello, World!'

@app.route('/register', methods=['GET','POST'])
def insert():
	error = None
	# Generate password hash, 12 rounds
	pwHash = bcrypt.generate_password_hash(request.form['password'])
	newUser = User(username=request.form['username'], email=request.form['email'], password=pwHash)
	with switch_collection(User, 'users') as toGet:
		try:
			if User.objects.get(username__exact = str(request.form['username'])):
				return 'User already exists. Choose a different username, or try logging in.'

		except DoesNotExist:
			with switch_collection(User, 'users') as toAdd:
				newUser.save(validate=True)
				return 'New user  %s added.' %user

@app.route('/delete', methods=['GET', 'POST'])
@login_required
def delete():
	if login_fresh() == True:
		try:
			error = None
			user = str(request.form['username'])
			with switch_collection(User, 'users') as toDel:
				User.objects(username=user).delete()
				logout_user()
				return 'User %s has been deleted' %user

		except DoesNotExist:
				return 'User %s does not exist.' %user

	else:
		return lm.unauthorized()

@app.route('/login', methods=['GET', 'POST'])
def login():
	user = request.form['username']
	password = request.form['password']
	with switch_collection(User, 'users') as toGet:
		try:
			search = User.objects.get(username__exact = user)
			if bcrypt.check_password_hash(search.password, password):
				login_user(search, remember=True, duration=datetime.timedelta(days=14))
				return search.to_json() + '\tLogged in as %s' %user

			else:
				return 'Invalid username or password. Try again.'

		except DoesNotExist:
			return 'User does not exist. Try again or register.'

@app.route('/logout', methods=['GET', 'POST'])
@login_required
def logout():
	logout_user()
	return "Logged out successfully."

if __name__ == '__main__':
	app.run()
