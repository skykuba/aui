import { Injectable } from '@angular/core';
import {ClientApiService} from '../client-api/client-api';
import {CreateOrUpdateClientInterface} from '../../models/client.model';
import { Observable } from 'rxjs';
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
}
