import { Injectable, Inject } from '@angular/core';
import { MatSnackBar } from '@angular/material';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { PasswordEntry } from './entry';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SESSION_STORAGE, WebStorageService } from 'angular-webstorage-service';
import aes from 'aes-js';

function pad(value) {
  const desiredLength = Math.ceil(value.length / 16) * 16;
  return value.padStart(desiredLength, 'x');
}

function unpad(value) {
  return value.replace(/^x*{/, '{');
}

export const apiUrl = 'http://127.0.0.1:5000/api/';

@Injectable({
  providedIn: 'root'
})
export class SyncService {
  key: Uint8Array;

  constructor(private http: HttpClient, private snackBar: MatSnackBar, @Inject(SESSION_STORAGE) private storage: WebStorageService) {
    if (this.storage.get('key')) {
      this.key = aes.utils.hex.toBytes(this.storage.get('key'));
    } else {
      // window.location.href = '/login';
    }
  }

  getEntries(): Observable<PasswordEntry[]> {
    return this.http.get<any>(apiUrl + 'sites')
      .pipe(
        map( data => {
          return data.map(dataEntry => {
            const aesCbc = new aes.ModeOfOperation.cbc(this.key, aes.utils.utf8.toBytes(dataEntry.id));
            // content is hex encoded and encrypted.  First decode hex, then decrypt,
            //   then get JSON string from bytes, then remove padding
            const entryJson = unpad(aes.utils.utf8.fromBytes(aesCbc.decrypt(aes.utils.hex.toBytes(dataEntry.content))));
            return new PasswordEntry(this, this.snackBar).deserialize({ id: dataEntry.id, ...JSON.parse(entryJson) });
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
    this.http.post(apiUrl + 'login', `username=test&password=test`, { headers })
      .subscribe(() => {
        this.http.post(apiUrl + 'token', `token=${token}`, { headers })
          .subscribe(() => {
            console.log('token success');
          });
      });
  }

  syncEntry(entry: PasswordEntry) {
    const aesCbc = new aes.ModeOfOperation.cbc(this.key, aes.utils.utf8.toBytes(entry.id));
    const data = new Uint8Array(aesCbc.encrypt(aes.utils.utf8.toBytes(pad(entry.serialize()))));
    return this.http.post(apiUrl + 'site/' + entry.id, data.buffer);
  }

  deleteEntry(id: string) {
    return this.http.delete(apiUrl + 'site/' + id);
  }

  logout() {
    this.storage.remove('username');
    this.storage.remove('key');
    return this.http.get(apiUrl + 'logout',
    {
      headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded'),
      responseType: 'text'
    });
  }
}
