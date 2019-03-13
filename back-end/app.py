from flask import Flask
from flask_pymongo import PyMongo

app = Flask(__name__)
app.config["MONOGO_URI"] = "mongodb://localhost:27017/keyper"
mongo = PyMongo(app)

@app.route('/')
def hello_world():
	return 'Helo world'

