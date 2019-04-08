import { Component, OnInit, Inject } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';

export class PGConfig {
  length: number;
  numbers: boolean;
  symbols: boolean;
  uppercase: boolean;
  excludeSimilarCharacters: boolean;
  strict: boolean;

  constructor() {
    this.strict = true;
    this.length = 20;
    this.numbers = true;
    this.symbols = true;
    this.uppercase = true;
    this.excludeSimilarCharacters = true;
  }
}

@Component({
  selector: 'app-pgdialog',
  templateUrl: './pgdialog.component.html',
  styleUrls: ['./pgdialog.component.scss']
})
export class PGDialogComponent implements OnInit {
  config: PGConfig;

  constructor(public dialogRef: MatDialogRef<PGDialogComponent>) {}

  ngOnInit() {
  }

}
