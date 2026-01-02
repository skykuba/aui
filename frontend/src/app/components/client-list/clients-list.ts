import { Component, OnInit, signal } from '@angular/core';
import { ClientService } from '../../services/client/client';
import { SimpleClientInterface, Client } from '../../models/client.model';
import {Router, RouterLink} from '@angular/router';

@Component({
  selector: 'app-clients-list',
  imports: [
    RouterLink
  ],
  templateUrl: './client-list.html',
  styleUrl: './client-list.css',
})
export class ClientsList implements OnInit {
  clients = signal<SimpleClientInterface[]>([]);

  constructor(private clientService: ClientService, private route: Router) {
  }

  ngOnInit() {
    this.loadClients();
  }

  loadClients() {
    this.clientService.getClients().subscribe({
      next: response => {
        let clientData = response.clients || response;
        if (Array.isArray(clientData)) {
          this.clients.set(clientData.map(client => Client.toSimple(new Client(client))));
        } else {
          console.error('Unexpected response format:', response);
        }
      },
      error: err => {
        console.error('Error loading clients:', err);
      }
    });
  }

  deleteClient(clientId: string) {
    this.clientService.deleteClient(clientId).subscribe({
      next: () => {
        const currentClients = this.clients();
        this.clients.set(currentClients.filter(client => client.id !== clientId));
        console.log('Client deleted successfully');
        console.log(`Deleted client ID: ${clientId}`);
      },
      error: err => {
        console.error('Error deleting client:', err);
      }
    });
  }

  updateClient(clientId: string) {
    this.route.navigate(['/clients',clientId,'edit'])
  }

  showClientInvoices(clientId:string) {
    this.route.navigate(['/clients',clientId,'invoices'])
  }
}
