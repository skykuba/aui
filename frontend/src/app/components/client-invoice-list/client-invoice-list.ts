import {Component, OnInit, signal, inject, DestroyRef} from '@angular/core';
import {SimpleInvoiceInterface} from '../../models/invoice.model';
import {InvoiceService} from '../../services/invoice/invoice';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {of} from 'rxjs';
import {switchMap, catchError} from 'rxjs/operators';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-client-invoice-list',
  imports: [
    RouterLink
  ],
  templateUrl: './client-invoice-list.html',
  styleUrl: './client-invoice-list.css',
})
export class ClientInvoiceList implements OnInit {
  invoices = signal<SimpleInvoiceInterface[]>([]);
  clientId: string | null = '';

  private invoiceService = inject(InvoiceService);
  private route = inject(ActivatedRoute);
  private destroyRef = inject(DestroyRef);

  ngOnInit(): void {
    const clientId = this.route.snapshot.paramMap.get('clientId');
    this.clientId = clientId;
    if (!clientId) {
      console.error('Client ID is required');
      return;
    }

    this.invoiceService.getClientsInvoices(clientId)
      .pipe(
        // switchMap zapobiega race kondition z odszyfrowywania uuid klienta
        switchMap(response => this.invoiceService.processInvoiceResponse(response)),
        catchError(err => {
          console.error('Error loading invoices', err);
          return of([]);
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe(invoices => this.invoices.set(invoices));
  }
}
