import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material';

export interface PasswordEntry {
  name?: string;
  url?: string;
  username?: string;
  email?: string;
  password?: string;
  tags?: [string];
  notes?: string;
}

const PASSWORD_DATA: PasswordEntry[] = [
  { name: 'Apple', url: 'apple.com', username: 'test@icloud.com', password: 'iloveapple' },
  { name: 'Google', url: 'google.com', username: 'test@gmail.com', password: 'ilovegoogle' },
  { name: 'Microsoft', url: 'microsoft.com', username: 'test@outlook.com', password: 'ilovemicrosoft' },
];

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  displayedColumns: string[] = ['name', 'url', 'username', 'email', 'password'];
  dataSource = new MatTableDataSource(PASSWORD_DATA);

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  constructor() { }

  ngOnInit() {
  }

}
