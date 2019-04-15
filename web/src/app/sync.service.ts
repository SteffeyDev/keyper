import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { PasswordEntry } from './entry';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import aes from 'aes-js';

function pad(value) {
  const desiredLength = Math.ceil(value.length / 16) * 16;
  return value.padStart(desiredLength, 'x');
}

function unpad(value) {
  return value.replace(/^x*{/, '{');
}

@Injectable({
  providedIn: 'root'
})
export class SyncService {
  // api = 'https://keyper.pro/site';
  api = 'http://127.0.0.1:5000/api/';
  key: Uint8Array;
  iv = [ 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36 ];

  constructor(private http: HttpClient) {
    this.setKey('testtesttesttesttesttesttesttest');
  }

  setKey(key: string) {
    this.key = aes.utils.utf8.toBytes(key);
  }

  getEntries(): Observable<PasswordEntry[]> {
    return this.http.get<any>(this.api + 'sites')
      .pipe(
        map( data => {
          return data.map(dataEntry => {
            const aesCbc = new aes.ModeOfOperation.cbc(this.key, aes.utils.utf8.toBytes(dataEntry.id));
            // content is hex encoded and encrypted.  First decode hex, then decrypt,
            //   then get JSON string from bytes, then remove padding
            const entryJson = unpad(aes.utils.utf8.fromBytes(aesCbc.decrypt(aes.utils.hex.toBytes(dataEntry.content))));
            return new PasswordEntry(this).deserialize({ id: dataEntry.id, ...JSON.parse(entryJson) });
          });
        }),
        catchError( error => {
          console.error(error);
          // window.location.href = '/login';
          return of([]);
        })
      );

    // temp for logging in
    const headers = new HttpHeaders({'Content-Type': 'application/x-www-form-urlencoded'});
    const token = prompt('token');
    this.http.post(this.api + 'login', `username=test&password=test`, { headers })
      .subscribe(() => {
        this.http.post(this.api + 'token', `token=${token}`, { headers })
          .subscribe(() => {
            console.log('token success');
          });
      });
  }

  syncEntry(entry: PasswordEntry) {
    const aesCbc = new aes.ModeOfOperation.cbc(this.key, aes.utils.utf8.toBytes(entry.id));
    const data = new Uint8Array(aesCbc.encrypt(aes.utils.utf8.toBytes(pad(entry.serialize()))));
    this.http.post(this.api + 'site/' + entry.id , data.buffer)
      .subscribe();
  }

  logout() {
    return this.http.get(this.api + 'logout');
  }
}
