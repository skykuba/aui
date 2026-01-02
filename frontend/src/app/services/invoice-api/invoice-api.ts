import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {InvoiceInterface, InvoiceListResponse, CreateOrUpdateInvoiceInterface} from '../../models/invoice.model';

@Injectable({
  providedIn: 'root',
})
export class InvoiceApiService {
  url: string = 'http://localhost:8080/api/invoices/';

  constructor(private http: HttpClient) {}

  getInvoices(): Observable<InvoiceListResponse> {
    return this.http.get<InvoiceListResponse>(this.url);
  }

  getClientsInvoices(clientId: string): Observable<InvoiceListResponse> {
    return this.http.get<InvoiceListResponse>(this.url + 'client/' + clientId);
  }

  getClientsInvoice(clientId: string, invoiceId: string): Observable<InvoiceInterface> {
    return this.http.get<InvoiceInterface>(this.url + 'client/' + clientId + '/' + invoiceId)
  }

  createInvoice(clientId: string, invoice: CreateOrUpdateInvoiceInterface): Observable<InvoiceInterface> {
    return this.http.post<InvoiceInterface>(this.url , invoice);
  }

  updateInvoice(clientId: string, invoiceId: string, invoice: CreateOrUpdateInvoiceInterface): Observable<InvoiceInterface> {
    return this.http.put<InvoiceInterface>(this.url + invoiceId, invoice);
  }

}
