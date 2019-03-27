import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TwofactorComponent } from './twofactor.component';

describe('TwofactorComponent', () => {
  let component: TwofactorComponent;
  let fixture: ComponentFixture<TwofactorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TwofactorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TwofactorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
