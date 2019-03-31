#!/usr/bin/python3

import re, os, datetime
import json
from flask import Flask, request, session
from flask_bcrypt import Bcrypt
import pyotp
from flask_jwt import JWT, jwt_required, current_identity
from mongoengine import *
from mongoengine.context_managers import switch_collection

app = Flask(__name__)
bcrypt = Bcrypt(app)

SECRET_KEY = os.environ.get("SECRET_KEY")
if not SECRET_KEY:
	raise ValueError("No secret key set in OS for API")

app.secret_key = SECRET_KEY

# db = keyper, mongodb://172.0.0.1:27017
#test database below. Eventually replace with real database
connect('keyper', host='mongodb://test:testUser1@ds161104.mlab.com:61104/practice')

# Encrypted site specific password blobs
class siteInfo(EmbeddedDocument):
	content = BinaryField()

class User(Document):
	username = StringField(max_length=64, required=True)
	email = StringField(max_length=64, required=True)
	password = StringField(required=True)
	sites = ListField(EmbeddedDocumentField(siteInfo))

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route('/register', methods=['GET','POST'])
def insert():
	error = None
	# Generate password hash, 12 rounds
	pwHash = bcrypt.generate_password_hash(request.form['password'])
	newUser = User(username=request.form['username'], email=request.form['email'], password=pwHash)
	with switch_collection(User, 'users') as toGet: #Need to verify if this is the correct way to handle this
		try:
			if User.objects.get(username__exact = str(request.form['username'])):
				return 'User already exists. Choose a different username, or try logging in.'

		except:
			with switch_collection(User, 'users') as toAdd:
				newUser.save(validate=True)
				return 'New user added'

@app.route('/delete', methods=['GET', 'POST'])
def delete():
		error = None
		user = str(request.form['username'])
		with switch_collection(User, 'users') as toDel:
			User.objects(username=user).delete()
			return 'User has been deleted'

@app.route('/login', methods=['GET', 'POST'])
def login():
	user = request.form['username']
	password = request.form['password']
	with switch_collection(User, 'users') as toGet:
		search = User.objects.get(username__exact = user)
		if bcrypt.check_password_hash(search.password, password):
			return 'Logged in as %s' %user

		else:
			return 'Incorrect password. Try again.'

@app.route('/logout', methods=['GET', 'POST'])
def logout():
	return True

@app.route('/retrieve',  methods=['GET', 'POST'])
def retrieve():
	user = str(request.form['username'])
	try:
		with switch_collection(User, 'users') as toGet:
			search = User.objects.get(username__exact = user)
			return search.to_json()
	except:
		return 'User not found.'

if __name__ == '__main__':
	app.run()
