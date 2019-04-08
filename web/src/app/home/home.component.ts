<<<<<<< Updated upstream
import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material';
=======
import { Component, OnInit, HostListener, ViewChild, ElementRef } from '@angular/core';
import { MatTableDataSource, MAT_TOOLTIP_DEFAULT_OPTIONS, MatTooltipDefaultOptions } from '@angular/material';
// import yiq from 'yiq';
>>>>>>> Stashed changes

export interface PasswordEntry {
  title?: string;
  url?: string;
  usertitle?: string;
  email?: string;
  password?: string;
  tags?: [string];
  notes?: string;
}

const PASSWORD_DATA: PasswordEntry[] = [
  { title: 'Apple', url: 'apple.com', usertitle: 'test@icloud.com', password: 'iloveapple' },
  { title: 'Google', url: 'google.com', usertitle: 'test@gmail.com', password: 'ilovegoogle' },
  { title: 'Microsoft', url: 'microsoft.com', usertitle: 'test@outlook.com', password: 'ilovemicrosoft' },
];

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  displayedColumns: string[] = ['title', 'url', 'username', 'email', 'password'];
  dataSource = new MatTableDataSource(PASSWORD_DATA);

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  constructor() { }

  ngOnInit() {
  }

}
