import { debounceTime, distinctUntilChanged, switchMapTo, takeUntil } from 'rxjs/operators';
import { observe } from 'rxjs-observe';
import Util from './util';
import { SyncService } from './sync.service';

function revisedRandId() {
  return Math.random().toString(36).replace(/[^a-z]+/g, '').substr(2, 10);
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

  constructor(private syncService: SyncService) {

    // When creating new entry
    this.tags = [];
    this.id = 'new';

    const { observables, proxy } = observe<PasswordEntry>(this);
    ['title', 'url', 'username', 'email', 'password', 'tags', 'notes'].forEach(prop => {
      observables[prop].pipe(
        debounceTime(400),
        distinctUntilChanged(),
      ).subscribe(() => this.sync());
    });
    return proxy;
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
    this.syncService.syncEntry(this);
  }

  showPassword() {
    this.passwordVisible = !this.passwordVisible;
  }

  copyPassword() {
    Util.copyToClipboard(this.password);
  }

  copyUsername() {
    Util.copyToClipboard(this.username);
  }

  copyEmail() {
    Util.copyToClipboard(this.email);
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
