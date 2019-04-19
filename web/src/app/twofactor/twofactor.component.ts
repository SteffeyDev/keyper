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
  public data: any = [];
  hasSavedAuthApp: boolean = false;
  authEntry: string;
  inputError: string;

  hashAndSendAuth(): void {
    console.log('entered hashAndSend');

    // https://Keyper.pro/api/login
    this.httpClient.post('http://13.59.202.229:5000/api/login', 'authcode=' + this.authEntry,
{
      headers: new HttpHeaders()
        .set('Content-Type', 'application/x-www-form-urlencoded')
    })
      // Success function
      .subscribe(() => {
        this.setSavedAuthCode(); // Stop QR code from showing after first use
        window.location.href = 'home';
},
      // Failure function
      () => { this.inputError = "Auth code error."; }
);
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

  getUsername(): string {
    // TODO: get username from session
    this.getFromSession("username");
    if (this.data["username"] != null)
      return this.data["username"].toString();
    else
      return "No Username";
  }

  getQRCode(): string {
    this.getFromSession("QRCode");
    if (this.data["QRCode"] != null)
      return this.data["QRCode"].toString();
    else
      return "";
  }

getSavedAuthCode(): boolean {
    this.getFromSession("SavedAuthCode");
    if (this.data["SavedAuthCode"] != null)
      return this.data["SavedAuthCode"];
    else
      return false;
  }

  // Save value for QR on 2fa page
  setSavedAuthCode(): void {
    this.saveInSession("SavedAuthCode", true); // Last part - put in session storage for home
  }

  resetSavedAuthApp(): void {
    this.saveInSession("SavedAuthCode", false);
    location.reload();
  }

  constructor(@Inject(SESSION_STORAGE) private storage: WebStorageService, private httpClient: HttpClient) {
    // Load variable on page refresh
    this.hasSavedAuthApp = this.getSavedAuthCode();
  }

  ngOnInit() {
  }

}
