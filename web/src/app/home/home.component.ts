import { Component, OnInit, HostListener, ViewChild, ElementRef } from '@angular/core';
import { MatTableDataSource, MAT_TOOLTIP_DEFAULT_OPTIONS, MatTooltipDefaultOptions } from '@angular/material';

function copyToClipboard(text: string) {
    const selBox = document.createElement('textarea');
    selBox.style.position = 'fixed';
    selBox.style.left = '0';
    selBox.style.top = '0';
    selBox.style.opacity = '0';
    selBox.value = text;
    document.body.appendChild(selBox);
    selBox.focus();
    selBox.select();
    document.execCommand('copy');
    document.body.removeChild(selBox);
}

export class PasswordEntry {
  title?: string;
  url?: string;
  username?: string;
  email?: string;
  password?: string;
  tags?: [string];
  notes?: string;
  passwordVisible: boolean;

  constructor(title: string, url: string, username: string, email: string, password: string) {
    this.title = title;
    this.url = url;
    this.username = username;
    this.email = email;
    this.password = password;

    this.passwordVisible = false;
  }

  showPassword() {
    this.passwordVisible = !this.passwordVisible;
  }

  copyPassword() {
    copyToClipboard(this.password);
  }

  copyUsername() {
    copyToClipboard(this.username);
  }

  copyEmail() {
    copyToClipboard(this.email);
  }

  openUrl() {
    let url = this.url;
    if (url.indexOf('http') === -1) {
      url = 'https://' + url;
    }
    window.open(url, '_blank');
  }
}

const PASSWORD_DATA: PasswordEntry[] = [
  new PasswordEntry('Apple', 'apple.com', null, 'test@icloud.com', 'iloveapple'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Microsoft', 'microsoft.com', null, 'test@outlook.com', 'iloveslowcomputers')
];

const myCustomTooltipDefaults: MatTooltipDefaultOptions = {
  showDelay: 500,
  hideDelay: 0,
  touchendHideDelay: 0,
};

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  providers: [
    {provide: MAT_TOOLTIP_DEFAULT_OPTIONS, useValue: myCustomTooltipDefaults}
  ],
})
export class HomeComponent implements OnInit {
  displayedColumns: string[] = ['title', 'url', 'username', 'email', 'password'];
  dataSource = new MatTableDataSource(PASSWORD_DATA);
  @ViewChild('filter') filterEl: ElementRef;

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  constructor() { }

  ngOnInit() {
  }

  @HostListener('document:keypress', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    const element = event.target as HTMLElement;
    if (element.tagName === 'BODY') {
      this.filterEl.nativeElement.value = '';
      this.applyFilter(event.key);
      this.filterEl.nativeElement.focus();
    }
  }
}
