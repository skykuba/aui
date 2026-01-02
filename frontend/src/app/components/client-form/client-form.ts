import { Component, OnInit } from '@angular/core';
import { ClientService } from '../../services/client/client';
import {ActivatedRoute, Router} from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import {CreateOrUpdateClientInterface} from '../../models/client.model';

@Component({
  selector: 'app-client-form',
  imports: [
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './client-form.html',
  styleUrl: './client-form.css',
})
export class ClientForm implements OnInit {
  clientForm: FormGroup;
  isEdit: boolean = false;
  clientId: string | null='';

  constructor(private fb: FormBuilder, private clientService: ClientService, private router: Router, private activatedRoute: ActivatedRoute) {
    this.clientForm = this.fb.group({
      client: this.fb.group({
        name: ['', Validators.required],
        nip: ['', Validators.required],
      }),
      address: this.fb.group({
        street: ['', Validators.required],
        buildingNumber: ['', Validators.required],
        city: this.fb.group({
          city: ['', Validators.required],
          state: ['', Validators.required],
          country: ['', Validators.required],
        }),
      }),
    });
  }

  ngOnInit() {
    const urlSegments = this.router.url.split('/');
    this.isEdit = urlSegments.includes('edit') && urlSegments.length > 2;
    if (this.isEdit) {
      this.clientId = this.activatedRoute.snapshot.paramMap.get('clientId');
      if (this.clientId!= null){
        this.loadClientDataById(this.clientId);
      }
    }
  }

  private loadClientDataById(id: string) {
    this.clientService.getClient(id).subscribe(client => {
      this.clientForm.patchValue({
        client: {
          name: client.name,
          nip: client.nip,
        },
        address: {
          street: client.address.street,
          buildingNumber: client.address.buildingNumber,
          city: {
            city: client.address.city.city,
            state: client.address.city.state,
            country: client.address.city.country,
          },
        },
      });
    });
  }

  onFormSubmit() {
    if (this.clientForm.valid) {
      const formValue = this.clientForm.value;
      if (this.isEdit && this.clientId) {
        const client: CreateOrUpdateClientInterface = {
          name: formValue.client.name,
          nip: formValue.client.nip,
          address: {
            street: formValue.address.street,
            buildingNumber: formValue.address.buildingNumber,
            city: {
              city: formValue.address.city.city,
              state: formValue.address.city.state,
              country: formValue.address.city.country,
            },
          },
        };
        this.clientService.updateClient(this.clientId, client).subscribe(() => {
          this.router.navigate(['/clients']);
        });
      } else {
        const createClient: CreateOrUpdateClientInterface = {
          name: formValue.client.name,
          nip: formValue.client.nip,
          address: {
            street: formValue.address.street,
            buildingNumber: formValue.address.buildingNumber,
            city: {
              city: formValue.address.city.city,
              state: formValue.address.city.state,
              country: formValue.address.city.country,
            },
          },
        };
        this.clientService.addClient(createClient).subscribe(() => {
          this.router.navigate(['/clients']);
        });
      }
    }
  }
}
