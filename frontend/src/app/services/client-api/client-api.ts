import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Client, ClientListResponse, CreateOrUpdateClientInterface} from '../../models/client.model';
import {Observable} from 'rxjs';
import {getClientApiUrl} from '../../config/api.config';

@Injectable({
  providedIn: 'root',
})
export class ClientApiService {
  url: string = getClientApiUrl();

  constructor(private http: HttpClient) { }

  getClients(): Observable<ClientListResponse> {
    return this.http.get<ClientListResponse>(this.url);
  }

  deleteClient(clientId: string): Observable<void> {
    return this.http.delete<void>(`${this.url}${clientId}`);
  }

  createClient(client: CreateOrUpdateClientInterface): Observable<Client> {
    return this.http.post<Client>(this.url, client);
  }

  getClient(clientId: string): Observable<Client> {
    return this.http.get<Client>(`${this.url}${clientId}`);
  }

  updateClient(clientId: string, client: CreateOrUpdateClientInterface): Observable<Client> {
    return this.http.put<Client>(`${this.url}${clientId}`, client);
  }
}
