#!/usr/bin/python3

import re, os, datetime
from flask import Flask, request, session
import pyotp
from flask_jwt import JWT, jwt_required, current_identity
from mongoengine import *
from mongoengine.context_managers import switch_collection

app = Flask(__name__)

SECRET_KEY = os.environ.get("SECRET_KEY")
if not SECRET_KEY:
	raise ValueError("No secret key set in OS for API")

# db = keyper, mongodb://172.0.0.1:27017
connect('keyper')

# Encrypted site specific password blobs
class siteInfo(EmbeddedDocument):
	content = BinaryField()

class User(Document):
	username = StringField(max_length=64, required=True)
	email = StringField(max_length=64, required=True)
	password = StringField(min_length=12, required=True)
	sites = ListField(EmbeddedDocumentField(siteInfo))

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route('/insert', methods=['GET','POST'])
def insert():
	error = None
	newUser = User(username=request.form['username'], email=request.form['email'], password=request.form['password'])
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
	return True

@app.route('/logout', methods=['GET', 'POST'])
def logout():
	return True

if __name__ == '__main__':
	app.run()
