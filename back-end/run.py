import re, os, datetime
from flask import Flask, request
from mongoengine import *
from mongoengine.context_managers import switch_collection

app = Flask(__name__)

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