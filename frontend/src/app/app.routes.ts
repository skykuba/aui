import { Routes } from '@angular/router';
import { ClientsList } from './components/client-list/clients-list';
import {CreateClient} from './components/create-client/create-client';
import {UpdateClient} from './components/update-client/update-client';
import {ClientInvoiceList} from './components/client-invoice-list/client-invoice-list';
import {InvoiceDetails} from './components/invoice-details/invoice-details';
import {CreateInvoice} from './components/create-invoice/create-invoice';
import {UpdateInvoice} from './components/update-invoice/update-invoice';

export const routes: Routes = [
  { path: '', redirectTo: '/clients', pathMatch: 'full' },
  { path: 'clients', component: ClientsList },
  { path: 'clients/new', component: CreateClient },
  { path: 'clients/:clientId/edit', component: UpdateClient },
  { path: 'clients/:clientId/invoices', component: ClientInvoiceList},
  { path: 'clients/:clientId/invoices/new', component: CreateInvoice },
  { path: 'clients/:clientId/invoices/:invoiceId', component: InvoiceDetails},
  { path: 'clients/:clientId/invoices/:invoiceId/edit', component: UpdateInvoice }
];
