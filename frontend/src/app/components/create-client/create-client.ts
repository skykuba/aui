import { Component, OnInit } from '@angular/core';
import {ClientService} from '../../services/client/client';
import {Router} from '@angular/router';
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
