import { Component, OnInit } from '@angular/core';
import { Inject, Injectable } from '@angular/core';
import { SESSION_STORAGE, WebStorageService } from 'angular-webstorage-service';
// import sha256 from '@angular/sha256';
// import * as shajs from 'sha.js';
import * as sha256 from 'sha256';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  hash: string;
  public data: any = [];

  hashPassword(password: string) {
    this.hash = sha256(password);
    // TODO: Send first 50 characters to server and the rest to home page
    // this.hash.substring(0,50); // First part
    // this.hash.substring(50); // Last part - put in session storage
  }

  // Webstorage Set and Get functions
  saveInSession(key, val): void {
    console.log('recieved= key:' + key + 'value:' + val);
    this.storage.set(key, val);
    this.data[key] = this.storage.get(key);
  }
  getFromSession(key): void {
    console.log('recieved= key:' + key);
    this.data[key] = this.storage.get(key);
    console.log(this.data);
  }

  constructor(@Inject(SESSION_STORAGE) private storage: WebStorageService) {

  }

  ngOnInit() {
  }

}
