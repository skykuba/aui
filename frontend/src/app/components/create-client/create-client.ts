import { Component } from '@angular/core';
import {ClientForm} from '../client-form/client-form';

@Component({
  selector: 'app-create-client',
  imports: [ClientForm],
  templateUrl: './create-client.html',
  styleUrl: './create-client.css',
})
export class CreateClient{
  constructor() {}


}
