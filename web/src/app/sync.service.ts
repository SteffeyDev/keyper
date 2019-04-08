import { Injectable } from '@angular/core';
import { PasswordEntry } from './entry';
import { PASSWORD_DATA } from './mock-passwords';

@Injectable({
  providedIn: 'root'
})
export class SyncService {

  constructor() { }

  getEntries(): PasswordEntry[] {
    return PASSWORD_DATA;
  }
}
