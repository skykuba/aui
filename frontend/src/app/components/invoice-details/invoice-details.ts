import {Component, inject, OnInit, signal, DestroyRef} from '@angular/core';
import {InvoiceService} from '../../services/invoice/invoice';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {Invoice} from '../../models/invoice.model';
import {catchError, of, switchMap} from 'rxjs';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-invoice-details',
  imports: [CommonModule, RouterLink],
  templateUrl: './invoice-details.html',
  styleUrl: './invoice-details.css',
  standalone: true,
})
export class InvoiceDetails implements OnInit {
  invoice = signal<Invoice | null>(null);
  clientId: string | null = '';
  invoiceId: string | null = '';

  private invoiceService = inject(InvoiceService);
  private route = inject(ActivatedRoute);
  private destroyRef = inject(DestroyRef);

  ngOnInit(): void {
    const clientId = this.route.snapshot.paramMap.get('clientId');
    const invoiceId = this.route.snapshot.paramMap.get('invoiceId');
    this.clientId = clientId;
    this.invoiceId = invoiceId;

    if (!clientId || !invoiceId) {
      console.error('Client ID or Invoice ID is missing');
      return;
    }

    this.invoiceService.getClientsInvoice(clientId, invoiceId)
      .pipe(
        switchMap(response => this.invoiceService.processInvoiceResponse(response)),
        catchError(err => {
          console.error('Error loading invoice details', err);
          return of([]);
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe(invoices => {
        if (invoices && invoices.length > 0) {
          this.invoice.set(new Invoice(invoices[0]));
        }
      });
  }
}
