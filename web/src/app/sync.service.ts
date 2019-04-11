import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { PasswordEntry } from './entry';
import { HttpClient } from '@angular/common/http';
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
    const aesCbc = new aes.ModeOfOperation.cbc(this.key, this.iv);
    return this.http.get<any>(this.api + 'sites')
      .pipe(
        map( data => {
          return data.map(dataEntry => {
            const entryJson = unpad(aes.utils.utf8.fromBytes(aesCbc.decrypt(dataEntry.content)));
            new PasswordEntry(this).deserialize({ id: dataEntry.id, ...JSON.parse(entryJson) });
          });
        }),
        catchError( error => {
          console.error(error);
          // window.location.href = '/login';
          return of([]);
        })
      );
  }

  syncEntry(entry: PasswordEntry) {
    if (entry.id === 'new') {
      delete entry.id;
    }

    const aesCbc = new aes.ModeOfOperation.cbc(this.key, this.iv);
    this.http.post(this.api + 'site', { id: entry.id, content: aesCbc.encrypt(aes.utils.utf8.toBytes(pad(entry.serialize()))) })
      .subscribe();
  }
}
