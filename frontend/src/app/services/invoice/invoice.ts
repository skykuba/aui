import { Injectable } from '@angular/core';
import {InvoiceApiService} from '../invoice-api/invoice-api';
import {forkJoin, of, throwError} from 'rxjs';
import {Invoice, CreateOrUpdateInvoiceInterface} from '../../models/invoice.model';
import {catchError, map} from 'rxjs/operators';
import {ClientService} from '../client/client';

@Injectable({
  providedIn: 'root',
})
export class InvoiceService {

  constructor(private invoiceApiService: InvoiceApiService, private clientService: ClientService) {
  }

  getInvoices() {
    return this.invoiceApiService.getInvoices();
  }

  getClientsInvoices(clientId: string) {
    if (clientId != null){
      return this.invoiceApiService.getClientsInvoices(clientId);
    }
    else {
      return throwError(() => new Error('Client ID required'));
    }
  }

  getClientsInvoice(clientId: string , id: string) {
    if(id != null) {
      return this.invoiceApiService.getClientsInvoice(clientId, id);
    }
    else {
      return throwError(() => new Error('Invoice ID required'));
    }
  }

  processInvoiceResponse(response: any) {
    // Normalizuj odpowiedź do tablicy
    let invoiceData = response.invoices || response;

    // Jeśli to pojedynczy obiekt, zawiń w tablicę
    if (!Array.isArray(invoiceData)) {
      invoiceData = [invoiceData];
    }

    const simpleInvoices = invoiceData.map((invoice: any) =>
      Invoice.toSimple(new Invoice(invoice))
    );

    const uuids = new Set([
      ...simpleInvoices.flatMap((inv: any) => [inv.clientUuid, inv.issuerUuid])
    ]);

    if (uuids.size === 0) {
      return of(simpleInvoices);
    }

    const clientRequests = Array.from(uuids).map(uuid =>
      this.clientService.getClient(uuid)
    );

    return forkJoin(clientRequests).pipe(
      map(clients => {
        const nameMap = new Map(
          Array.from(uuids).map((uuid, index) => [uuid, clients[index].name])
        );

        return simpleInvoices.map((inv: any) => ({
          ...inv,
          clientName: nameMap.get(inv.clientUuid) || '',
          issuerName: nameMap.get(inv.issuerUuid) || ''
        }));
      }),
      catchError(err => {
        console.error('Error fetching client names', err);
        return of(simpleInvoices);
      })
    );
  }

  createInvoice(clientId: string, invoice: CreateOrUpdateInvoiceInterface) {
    return this.invoiceApiService.createInvoice(clientId, invoice);
  }

  updateInvoice(clientId: string, invoiceId: string, invoice: CreateOrUpdateInvoiceInterface) {
    return this.invoiceApiService.updateInvoice(clientId, invoiceId, invoice);
  }

}
