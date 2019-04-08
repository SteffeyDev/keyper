import Util from './util';

function revisedRandId() {
  return Math.random().toString(36).replace(/[^a-z]+/g, '').substr(2, 10);
}

export class PasswordEntry {
  id: string;
  title?: string;
  url?: string;
  username?: string;
  email?: string;
  password?: string;
  tags?: string[];
  notes?: string;
  passwordVisible: boolean;

  constructor(title: string, url: string, username: string, email: string, password: string) {
    this.title = title;
    this.url = url;
    this.username = username;
    this.email = email;
    this.password = password;
    this.tags = [];
    this.id = revisedRandId();

    this.passwordVisible = !password;
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
