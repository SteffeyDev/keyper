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
  MatChipsModule, MatDialogModule, MatCheckboxModule } from '@angular/material';
import { DndModule } from 'ngx-drag-drop';
import { FormsModule } from '@angular/forms';

import { FlexLayoutModule } from '@angular/flex-layout';
import { InputComponent } from './input/input.component';
import { PGDialogComponent } from './pgdialog/pgdialog.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,
    TwoFactorComponent,
    HomeComponent,
    InputComponent,
    PGDialogComponent
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
    HttpClientModule
  ],
  entryComponents: [
    PGDialogComponent
  ],
  providers: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
