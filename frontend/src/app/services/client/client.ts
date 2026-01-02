import { Injectable } from '@angular/core';
import {ClientApiService} from '../client-api/client-api';
import {CreateOrUpdateClientInterface} from '../../models/client.model';
import { Observable, of } from 'rxjs';
import { Client } from '../../models/client.model';

@Injectable({
  providedIn: 'root',
})
export class ClientService {

  constructor(private clientApiService: ClientApiService) {}

  getClients() {
    return this.clientApiService.getClients();
  }

  deleteClient(clientId: string) {
    return this.clientApiService.deleteClient(clientId);
  }

  addClient(client: CreateOrUpdateClientInterface) {
    return this.clientApiService.createClient(client);
  }

  getClient(clientId: string): Observable<Client> {
    return this.clientApiService.getClient(clientId);
  }

  updateClient(clientId: string, client: CreateOrUpdateClientInterface): Observable<Client> {
    return this.clientApiService.updateClient(clientId, client);
  }

  processInvoiceResponse(response: any) {
    // Normalizuj odpowiedź do tablicy
    let clientData = response.clients || response;

    // Jeśli to pojedynczy obiekt, zawiń w tablicę
    if (!Array.isArray(clientData)) {
      clientData = [clientData];
    }

    // Konwertuj dane do obiektu Client i następnie do uproszczonej formy
    const simpleClients = clientData.map((client: any) =>
      Client.toSimple(new Client(client))
    );

    return of(simpleClients);
  }
}
