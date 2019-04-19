import { Component, OnInit } from '@angular/core';
import { Inject, Injectable } from '@angular/core';
import { SESSION_STORAGE, WebStorageService } from 'angular-webstorage-service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { sha512 } from 'js-sha512';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  hash: string;
  usernameEntry = '';
  passwordEntry = '';
  inputError: string;

  hashAndSendPassword(): void {
    console.log('entered hashAndSend');
    this.hash = sha512(this.passwordEntry);

    // https://Keyper.pro/api/login
    this.httpClient.post('http://127.0.0.1:5000/api/login', `username=${this.usernameEntry}&password=${this.hash.substring(0, 32)}`,
    {
      headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
    })
      // Success function
      .subscribe(() => {
        this.setUsername();
        this.setPassword();
        window.location.href = '2fa';
      },
      // Failure function
      () => {
        this.inputError = 'Username or password incorrect.';
      });
  }

  // Webstorage Set and Get functions
  saveInSession(key, val): void {
    console.log('recieved= key: ' + key + ' value: ' + val);
    this.storage.set(key, val);
  }

  setUsername(): void {
    this.saveInSession('username', this.usernameEntry);
  }

  setPassword(): void {
    this.saveInSession('key', this.hash.substring(32)); // Last part - put in session storage for home
  }

  constructor(@Inject(SESSION_STORAGE) private storage: WebStorageService, private httpClient: HttpClient) { }

  ngOnInit() { }
}
