import { Component, OnInit } from '@angular/core';
import { Inject, Injectable } from '@angular/core';
import { SESSION_STORAGE, WebStorageService } from 'angular-webstorage-service';

@Component({
  selector: 'app-twofactor',
  templateUrl: './twofactor.component.html',
  styleUrls: ['./twofactor.component.scss']
})
export class TwoFactorComponent implements OnInit {
  public data: any = [];
  hasSavedAuthApp: boolean = false;
  stringForQRCode: string = "example";

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
    // TODO: get username from server
    this.getSession("username");
    if (this.data["username"] != null)
      return this.data["username"].toString();
    else
      return "No Username";
  }

  constructor(@Inject(SESSION_STORAGE) private storage: WebStorageService) { }

  ngOnInit() {
  }

}
