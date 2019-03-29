import { Component, OnInit, Input, ViewChild, ElementRef } from '@angular/core';
import { PasswordEntry } from '../home/home.component';

@Component({
  selector: 'app-input',
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.scss']
})
export class InputComponent implements OnInit {
  @Input() entry: PasswordEntry;
  @Input() field: string;
  @ViewChild('str') inputEl: ElementRef;
  editing: boolean;

  constructor() {
    this.editing = false;
  }

  ngOnInit() {
  }

  edit() {
    this.editing = true;
    setTimeout(() => this.inputEl.nativeElement.focus(), 0);
  }

  save(str) {
    this.entry[this.field] = str;
    this.editing = false;
  }
}
