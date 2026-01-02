import { Component } from '@angular/core';
import {ClientForm} from '../client-form/client-form';

@Component({
  selector: 'app-update-client',
  imports: [ClientForm],
  templateUrl: './update-client.html',
  styleUrl: './update-client.css',
})
export class UpdateClient {
  constructor() {
  }

}
