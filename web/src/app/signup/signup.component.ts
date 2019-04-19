import { Component, OnInit } from '@angular/core';
import { Inject, Injectable } from '@angular/core';
import { SESSION_STORAGE, StorageService } from 'angular-webstorage-service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { sha512 } from 'js-sha512';
import { catchError, retry } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {
  hash: string;
  usernameEntry = '';
  emailEntry = '';
  passwordEntry = '';
  passwordConfEntry = '';
  inputError: string;

  hashAndSendPassword(): void {
    // Check if passwords match
    if (this.passwordEntry !== this.passwordConfEntry) {
      this.inputError = 'Please confirm password.';
      return;
    }
    this.hash = sha512(this.passwordEntry);

    this.httpClient.post('http://127.0.0.1:5000/api/register',
      `username=${this.usernameEntry}&email=${this.emailEntry}&password=${this.hash.substring(0, 50)}`,
    {
      headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded'),
      responseType: 'text'
    })
      .pipe(
        catchError(() => throwError('Invalid email or Username taken'))
      )
      // Success function
      .subscribe((totpUri: string) => {
        this.setUsername();
        this.setPassword();
        this.setQRCode(totpUri);
        window.location.href = '2fa';
      },
      // Failure function
      (err) => {
        this.inputError = err;
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
    this.saveInSession('key', this.hash.substring(64)); // Last part - put in session storage for home
  }

  // Save value for QR on 2fa page
  setQRCode(qrcode: string): void {
    this.saveInSession('QRCode', qrcode);
  }

  constructor(@Inject(SESSION_STORAGE) private storage: StorageService, private httpClient: HttpClient) { }

  ngOnInit() { }

}
