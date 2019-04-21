import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { TwoFactorComponent } from './twofactor/twofactor.component';
import { HomeComponent } from './home/home.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule, MatFormFieldModule, MatInputModule, MatSliderModule, MatSlideToggleModule,
  MatMenuModule, MatTableModule, MatSortModule, MatProgressBarModule, MatIconModule, MatTooltipModule,
  MatChipsModule, MatDialogModule, MatCheckboxModule, MatSnackBarModule } from '@angular/material';
import { DndModule } from 'ngx-drag-drop';
import { FormsModule } from '@angular/forms';

import { FlexLayoutModule } from '@angular/flex-layout';
import { InputComponent } from './input/input.component';
import { PGDialogComponent } from './pgdialog/pgdialog.component';
import { NotesDialogComponent } from './notes-dialog/notes-dialog.component';

// Webstorage
import { StorageServiceModule } from 'angular-webstorage-service';
// QR Code Gen
import { QRCodeModule } from 'angularx-qrcode';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,
    TwoFactorComponent,
    HomeComponent,
    InputComponent,
    PGDialogComponent,
    NotesDialogComponent
  ],
  imports: [
    FlexLayoutModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatMenuModule,
    MatTableModule,
    MatSortModule,
    MatProgressBarModule,
    MatIconModule,
    MatTooltipModule,
    MatChipsModule,
    DndModule,
    FormsModule,
    MatDialogModule,
    MatCheckboxModule,
    HttpClientModule,
    MatSnackBarModule,
    StorageServiceModule,
    QRCodeModule
  ],
  entryComponents: [
    PGDialogComponent,
    NotesDialogComponent
  ],
  providers: [
    BrowserModule,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
