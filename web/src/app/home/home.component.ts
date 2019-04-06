import { Component, OnInit, HostListener, ViewChild, ElementRef } from '@angular/core';
import { MatTableDataSource, MAT_TOOLTIP_DEFAULT_OPTIONS, MatTooltipDefaultOptions } from '@angular/material';
import yiq from 'yiq';

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

const PASSWORD_DATA: PasswordEntry[] = [
  new PasswordEntry('Apple', 'apple.com', null, 'test@icloud.com', 'iloveapple'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Microsoft', 'microsoft.com', null, 'test@outlook.com', 'iloveslowcomputers'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
  new PasswordEntry('Google', 'google.com', null, 'test@gmail.com', 'ilovegoogle'),
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
  displayedColumns: string[] = ['tags', 'title', 'url', 'username', 'email', 'password', 'delete'];
  dataSource = new MatTableDataSource(PASSWORD_DATA);
  tags: string[] = [];
  newTag: boolean;
  colorMap = {};
  sourceColorList = ['#ff0000', '#f58231', '#ffe119', '#bcf60c', '#3cb44b', '#46f0f0',
    '#4363d8', '#911eb4', '#f032e9', '#000075', '#aaffc3', '#e6beff', '#8b0000', '#ff0033', '#4b0082', '#5c4033', '#ff69b4'];

  @ViewChild('filter') filterEl: ElementRef;
  @ViewChild('newTagText') newTagEl: ElementRef;

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  constructor() { }

  ngOnInit() {
    this.tags = [];
    this.allTags.forEach(tag => { this.colorMap[tag] = this.sourceColorList.pop(); });
  }

  getColor(tag) {
    return this.colorMap[tag];
  }

  getTextColor(tag) {
    return yiq(this.getColor(tag) || '#d3d3d3');
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

  onDrop({ data }, entry) {
    entry.addTag(data);
  }

  showNewTag() {
    this.newTag = true;
    setTimeout(() => this.newTagEl.nativeElement.focus(), 0);
  }

  addTag(newTag) {
    this.tags.push(newTag);
    this.colorMap[newTag] = this.sourceColorList.pop();
    this.newTag = false;
  }

  get visibleTags() {
    return Array.from(PASSWORD_DATA.reduce((set, entry) => { entry.tags.forEach(tag => set.add(tag)); return set; }, new Set()));
  }

  get allTags() {
    return Array.from(PASSWORD_DATA.reduce((set, entry) => { entry.tags.forEach(tag => set.add(tag)); return set; }, new Set(this.tags)));
  }

  get tagColumnWidth() {
    return 14 * Math.ceil(this.visibleTags.length / 2);
  }
}
