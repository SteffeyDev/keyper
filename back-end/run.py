import re, os, datetime
from flask import Flask
from mongoengine import *

app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello, World!'

# Database = keyper
connect('keyper')

# Encrypted site specific password blobs
class siteInfo(EmbeddedDocument):
	content = BinaryField()

class User(Document):
	username = StringField(max_length=64, required=True)
	email = StringField(max_length=64, required=True)
	password = StringField(min_length=12, required=True)
	sites = ListField(EmbeddedDocumentField(siteInfo))

