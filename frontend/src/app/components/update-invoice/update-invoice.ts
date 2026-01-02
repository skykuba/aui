import { Component } from '@angular/core';
import { InvoiceForm } from '../invoice-form/invoice-form';

@Component({
  selector: 'app-update-invoice',
  imports: [InvoiceForm],
  templateUrl: './update-invoice.html',
  styleUrl: './update-invoice.css',
  standalone: true,
})
export class UpdateInvoice {
  constructor() {}
}

