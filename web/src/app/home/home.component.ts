import { Component, OnInit, HostListener, ViewChild, ElementRef, Inject, } from '@angular/core';
import { MatTableDataSource, MAT_TOOLTIP_DEFAULT_OPTIONS, MatTooltipDefaultOptions, MatDialog,
  MatTable, MatSnackBar } from '@angular/material';
import { PGDialogComponent, PGConfig } from '../pgdialog/pgdialog.component';
import { NotesDialogComponent } from '../notes-dialog/notes-dialog.component';
import yiq from 'yiq';
import { generate } from 'generate-password-browser';
import { PasswordEntry } from '../entry';
import { SyncService } from '../sync.service';
import Utils from '../util';

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
  displayedColumns: string[] = ['tags', 'title', 'url', 'username', 'email', 'password', 'notes', 'delete'];
  dataSource = new MatTableDataSource<PasswordEntry>();
  tags: string[] = [];
  newTag: boolean;
  colorMap = {};
  password: string;
  passwordConfig: PGConfig;
  sourceColorList = ['#ff0000', '#f58231', '#ffe119', '#bcf60c', '#3cb44b', '#46f0f0',
    '#4363d8', '#911eb4', '#f032e9', '#000075', '#aaffc3', '#e6beff', '#8b0000', '#ff0033', '#4b0082', '#5c4033', '#ff69b4'];

  @ViewChild('filter') filterEl: ElementRef;
  @ViewChild('newTagText') newTagEl: ElementRef;
  @ViewChild(MatTable) table: MatTable<any>;

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  constructor(public dialog: MatDialog, private syncService: SyncService, private snackBar: MatSnackBar) { }

  logout() {
    this.syncService.logout()
      .subscribe(() => { window.location.href = 'login'; });
  }

  getEntries(): void {
    this.syncService.getEntries()
      .subscribe(entries => {
        this.dataSource.data = entries;
        this.allTags.forEach(tag => this.colorMap[tag] = this.sourceColorList.pop());
      });
  }

  ngOnInit() {
    this.getEntries();
    this.tags = [];
    this.passwordConfig = new PGConfig();
    this.generateNewPassword();
  }

  addEntry() {
    this.dataSource.data.unshift(new PasswordEntry(this.syncService, this.snackBar));
    this.table.renderRows();
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
    entry.sync();
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

  removeTag(entry, tag) {
    if (confirm(`Are you sure you want to remove the tag '${tag}' from '${entry.title || entry.url}'?`)) {
      entry.tags = entry.tags.filter(t => t !== tag);
      entry.sync();
    }
  }

  get visibleTags() {
    return Array.from(this.dataSource.data.reduce((set, entry) => { entry.tags.forEach(tag => set.add(tag)); return set; }, new Set()));
  }

  get allTags() {
    return Array.from(this.dataSource.data
      .reduce((set, entry) => { entry.tags.forEach(tag => set.add(tag)); return set; }, new Set(this.tags)));
  }

  get tagColumnWidth() {
    return 14 * Math.ceil(this.visibleTags.length / 2);
  }

  generateNewPassword() {
    this.password = generate(this.passwordConfig);
  }

  copyPassword() {
    Utils.copyToClipboard(this.password);
  }

  showPGDialog() {
    const dialogRef = this.dialog.open(PGDialogComponent, {
      width: '300px',
      position: {
        top: '130px',
        right: '100px'
      },
      autoFocus: false
    });
    dialogRef.componentInstance.config = this.passwordConfig;

    dialogRef.afterClosed().subscribe(result => {
      this.passwordConfig = dialogRef.componentInstance.config;
      this.password = generate(this.passwordConfig);
    });
  }

  showNotesDialog(entry) {
    const dialogRef = this.dialog.open(NotesDialogComponent, {
      width: '300px',
    });
    dialogRef.componentInstance.notes = entry.notes;
    dialogRef.componentInstance.title = entry.title || entry.url;

    dialogRef.afterClosed().subscribe(result => {
      entry.notes = dialogRef.componentInstance.notes;
      entry.sync();
    });
  }

  deleteEntry(id) {
    const removedEntry = this.dataSource.data.filter(entry => entry.id === id)[0];
    if (confirm(`Are you sure you want to delete the password to the site ${removedEntry.url || removedEntry.title}?`)) {
      this.dataSource.data = this.dataSource.data.filter(entry => entry.id !== id);
      this.table.renderRows();
      this.syncService.deleteEntry(id)
        .subscribe(() => null, () => {
          this.dataSource.data.unshift(removedEntry);
          this.table.renderRows();
          this.snackBar.open('Could not delete', null, { duration: 5000 });
        });
    }
  }
}
