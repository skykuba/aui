import { Component, OnInit } from '@angular/core';
import { InvoiceForm } from '../invoice-form/invoice-form';
import { ActivatedRoute } from '@angular/router';
import { InvoiceService } from '../../services/invoice/invoice';
import { CreateOrUpdateInvoiceInterface } from '../../models/invoice.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-update-invoice',
  imports: [InvoiceForm, CommonModule],
  templateUrl: './update-invoice.html',
  styleUrl: './update-invoice.css',
  standalone: true,
})
export class UpdateInvoice implements OnInit {
  invoice: CreateOrUpdateInvoiceInterface | null = null;

  constructor(
    private route: ActivatedRoute,
    private invoiceService: InvoiceService
  ) {}

  ngOnInit(): void {
    const invoiceId = this.route.snapshot.paramMap.get('invoiceId');
    const clientId = this.route.snapshot.paramMap.get('clientId');
    if (invoiceId && clientId) {
      this.invoiceService
        .getClientsInvoice(clientId, invoiceId)
        .subscribe((invoice) => {
          this.invoice = invoice;
        });
    }
  }
}
