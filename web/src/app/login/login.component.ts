import { Component, OnInit } from '@angular/core';
import { Inject, Injectable } from '@angular/core';
import { SESSION_STORAGE, WebStorageService } from 'angular-webstorage-service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import * as sha256 from 'sha256';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  hash: string;
  public data: any = [];
  usernameEntry: string = "";
  passwordEntry: string = "";
  inputError: string;

  hashAndSendPassword(): void {
    console.log('entered hashAndSend');
    this.hash = sha256(this.passwordEntry);
    // Send first 50 characters to server and the rest to home page

    // https://Keyper.pro/api/login
    this.httpClient.post('http://13.59.202.229:5000/api/login', 'username=' + this.usernameEntry + '&password=' + this.hash.substring(0,50),
{
      headers: new HttpHeaders()
        .set('Content-Type', 'application/x-www-form-urlencoded')
    })
      // Success function
      // TODO: Get and save string to be displayed in QR Code using setQRCode function
      .subscribe(() => {
        this.setUsername();
        this.setPassword();
        window.location.href = '2fa'; 
},
      // Failure function
      () => { this.inputError = "Username or password incorrect."; }
);
  }

  // Webstorage Set and Get functions
  saveInSession(key, val): void {
    console.log('recieved= key: ' + key + ' value: ' + val);
    this.storage.set(key, val);
    this.data[key] = this.storage.get(key);
  }
  getFromSession(key): void {
    console.log('recieved= key: ' + key);
    this.data[key] = this.storage.get(key);
    console.log(this.data);
  }

  setUsername(): void {
    this.saveInSession("username", this.usernameEntry);
  }
  setPassword(): void {
    this.saveInSession("hashedPass", this.hash.substring(50)); // Last part - put in session storage for home
  }

  // Save value for QR on 2fa page
  setQRCode(qrcode: string): void {
    this.saveInSession("QRCode", qrcode ? qrcode!=null : "");
  }


  constructor(@Inject(SESSION_STORAGE) private storage: WebStorageService, private httpClient: HttpClient) {

  }

  ngOnInit() {
  }

}
