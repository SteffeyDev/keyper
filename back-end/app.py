from flask import Flask, request
from mongokit import *
import re
import datetime

app = Flask(__name__)
app.config.from_object(__name__)

@app.route('/')
def hello_world():
	return 'Helo world'

MONGODB_HOST = 'localhost'
MONGODB_PORT = 27017

connection = Connection(app.config['MONGODB_HOST'], app.config['MONGODB_PORT'])

@connection.register
class User(Document):
	__collection__ = 'Users'
	__database__ = 'keyper'
	structure = {
		'username': str,
		'email': str,
		'password': str,
		'sites' = {
			'title': str,
			'username': str,
			'email': str,
			'sitePass': str,
			'url': str,
			'tags' : [str],
			'notes' : str,
			'dateAdded': datetime.datetime,
			'dateLastUsed': datetime.datetime
		}
	}
	required_fields = ['username', 'email', 'password']
	default_values = {
		'sites.dateAdded': datetime.datetime.utcnow
	}
	validators = {
		'email': email_validator,
		'sites.email': email_validator
	}
	use_dot_notation = True

def email_validator(value):
   email = re.compile(r'(?:^|\s)[-a-z0-9_.]+@(?:[-a-z0-9]+\.)+[a-z]{2,6}(?:\s|$)',re.IGNORECASE)
   if not email.match(value):
      raise ValidationError('%s is not a valid email' % value)
