import { Component, OnInit, Input, ViewChild, ElementRef, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { PasswordEntry } from '../home/home.component';

const customValueProvider = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => InputComponent),
    multi: true
};

@Component({
  selector: 'app-input',
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.scss'],
  providers: [ customValueProvider ]
})
export class InputComponent implements ControlValueAccessor {
  @ViewChild('str') inputEl: ElementRef;
  @Input() name: string;
  editing: boolean;
  value: string;
  propagateChange: any = () => {};

  constructor() {
    this.editing = false;
  }

  writeValue(value: any) {
    if (value) {
     this.value = value;
    }
  }

  registerOnChange(fn) {
    this.propagateChange = fn;
  }
  registerOnTouched(fn: () => void): void { }

  edit() {
    this.editing = true;
    setTimeout(() => this.inputEl.nativeElement.focus(), 0);
  }

  save() {
    this.propagateChange(this.value);
    this.editing = false;
  }
}
