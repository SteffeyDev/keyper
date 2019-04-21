import { debounceTime, distinctUntilChanged, switchMapTo, takeUntil } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material';
import { observe } from 'rxjs-observe';
import Util from './util';
import { SyncService } from './sync.service';

function makeid(length) {
  let text = '';
  const possible = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

  for (let i = 0; i < length; i++) {
    text += possible.charAt(Math.floor(Math.random() * possible.length));
  }

  return text;
}

interface Serializable<T> {
    deserialize(input: any): T;
}

export class PasswordEntry implements Serializable<PasswordEntry> {
  id: string;
  title?: string;
  url?: string;
  username?: string;
  email?: string;
  password?: string;
  tags?: string[];
  notes?: string;
  passwordVisible = true;
  lastSync: string;

  constructor(private syncService: SyncService, private snackBar: MatSnackBar) {

    // When creating new entry
    this.tags = [];
    this.id = makeid(16);
  }

  deserialize(input: any) {
    this.id = input.id;
    this.title = input.title;
    this.url = input.url;
    this.username = input.username;
    this.email = input.email;
    this.password = input.password;
    this.tags = input.tags || [];
    this.notes = input.notes;

    this.lastSync = this.serialize();
    this.passwordVisible = false;

    return this;
  }

  serialize() {
    return JSON.stringify({
      id: this.id,
      title: this.title,
      url: this.url,
      username: this.username,
      email: this.email,
      password: this.password,
      tags: this.tags,
      notes: this.notes
    });
  }

  sync() {
    setTimeout(() => {
      if (this.serialize() !== this.lastSync) {
        this.lastSync = this.serialize();
        this.syncService.syncEntry(this)
          .subscribe(() => this.snackBar.open('Saved', null, { duration: 2000 }));
      }
    }, 50);
  }

  showPassword() {
    this.passwordVisible = !this.passwordVisible;
  }

  copyPassword() {
    Util.copyToClipboard(this.password);
    this.snackBar.open('Password copied to clipboard', null, { duration: 2000 });
  }

  copyUsername() {
    Util.copyToClipboard(this.username);
    this.snackBar.open('Username copied to clipboard', null, { duration: 2000 });
  }

  copyEmail() {
    Util.copyToClipboard(this.email);
    this.snackBar.open('Email copied to clipboard', null, { duration: 2000 });
  }

  openUrl() {
    let url = this.url;
    if (url.indexOf('http') === -1) {
      url = 'https://' + url;
    }
    window.open(url, '_blank');
  }

  deleteEntry() {
    if (confirm('Are you sure you want to remove this password entry? You will not be able to recover it if you continue')) {
      // delete entry, refresh table
    }
  }

  addTag(tag) {
    if (this.tags.indexOf(tag) === -1) {
      this.tags.push(tag);
    }
  }
}
