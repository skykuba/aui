import { Component } from '@angular/core';
import { InvoiceForm } from '../invoice-form/invoice-form';

@Component({
  selector: 'app-create-invoice',
  imports: [InvoiceForm],
  templateUrl: './create-invoice.html',
  styleUrl: './create-invoice.css',
  standalone: true,
})
export class CreateInvoice {
  constructor() {}
}

