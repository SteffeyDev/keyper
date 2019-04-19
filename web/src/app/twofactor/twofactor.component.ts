import { Component, OnInit } from '@angular/core';
import { Inject, Injectable } from '@angular/core';
import { SESSION_STORAGE, WebStorageService } from 'angular-webstorage-service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-twofactor',
  templateUrl: './twofactor.component.html',
  styleUrls: ['./twofactor.component.scss']
})
export class TwoFactorComponent implements OnInit {
  totpUri: string;
  username: string;
  authEntry: string;
  inputError: string;

  sendAuth(): void {
    // https://Keyper.pro/api/login
    this.httpClient.post('http://127.0.0.1:5000/api/token', `token=${this.authEntry}`,
    {
      headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded'),
      responseType: 'text'
    })
      // Success function
      .subscribe(() => {
        this.storage.remove('QRCode');
        window.location.href = '/home';
      },
      // Failure function
      () => {
        this.inputError = 'Auth code error.';
      });
  }

  constructor(@Inject(SESSION_STORAGE) private storage: WebStorageService, private httpClient: HttpClient) {
    this.totpUri = this.storage.get('QRCode');
    this.username = this.storage.get('username');
  }

  ngOnInit() { }

}
