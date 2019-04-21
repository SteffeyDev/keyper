import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-notes-dialog',
  templateUrl: './notes-dialog.component.html',
  styleUrls: ['./notes-dialog.component.scss']
})
export class NotesDialogComponent implements OnInit {
  notes: string;
  title: string;

  constructor() { }

  ngOnInit() {
  }

}
