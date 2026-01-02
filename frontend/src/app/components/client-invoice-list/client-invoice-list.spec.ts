import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientInvoiceList } from './client-invoice-list';

describe('ClientInvoiceList', () => {
  let component: ClientInvoiceList;
  let fixture: ComponentFixture<ClientInvoiceList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientInvoiceList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientInvoiceList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
