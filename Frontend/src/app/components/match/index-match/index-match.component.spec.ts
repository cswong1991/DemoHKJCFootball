import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IndexMatchComponent } from './index-match.component';

describe('IndexMatchComponent', () => {
  let component: IndexMatchComponent;
  let fixture: ComponentFixture<IndexMatchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IndexMatchComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IndexMatchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
