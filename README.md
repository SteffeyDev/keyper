# keyper - Password Manager

## Features
* 2-Factor Authentication
* Password Generation
* Ability to automatically go to login site from app
* Auto copy password to clipboard
* Add/edit logins
* Add tags
* Client side search

### Potential Features
* Send login/password to other users
* Android Autofill
* Chrome Extension
* Deceased password recovery

## Data Contained in App
* Email
* Username / Site Login
* Site URL
* Password
* Notes
* Tags

## Tech Stack

### Backend
* Database - MongoDB
* API - Python (Flask)
* Mongoengine

### Web Front End
* AngularJS
* Bootstrap?

### Android App
* Develop with Android Studio
* Java

## Deployment
* Clone github
  ```
  $ cd /opt
  $ git clone https://github.com/SteffeyDev/keyper.git
  ```
* Install components
  ```
  $ cd /opt/keyper
  $ pip install -r requironments.txt
  $ cd web
  $ yarn install
  ```
* Build website
  ```
  $ cd /opt/keyper/web
  $ yarn run build
  ```
* Start server
  ```
  $ cd /opt/keyper
  $ cp keyper.service /etc/systemd/system/
  $ systemctl enable keyper.service
  $ systemctl start keyper.service
  ```
