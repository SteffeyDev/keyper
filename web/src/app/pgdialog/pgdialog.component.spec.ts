import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PGDialogComponent } from './pgdialog.component';

describe('PGDialogComponent', () => {
  let component: PGDialogComponent;
  let fixture: ComponentFixture<PGDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PGDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PGDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
