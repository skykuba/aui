import { Component, OnInit } from '@angular/core';
import { ClientService } from '../../services/client/client';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { CreateOrUpdateClientInterface } from '../../models/client.model';

@Component({
  selector: 'app-client-form',
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './client-form.html',
  styleUrl: './client-form.css',
})
export class ClientForm implements OnInit {
  clientForm: FormGroup;
  isEdit = false;
  clientId: string | null = null;

  constructor(
    private fb: FormBuilder,
    private clientService: ClientService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {
    this.clientForm = this.createForm();
  }

  ngOnInit(): void {
    this.checkIfEditing();
  }

  private createForm(): FormGroup {
    return this.fb.group({
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

  private checkIfEditing(): void {
    const urlSegments = this.router.url.split('/');
    this.isEdit = urlSegments.includes('edit') && urlSegments.length > 2;
    if (this.isEdit) {
      this.clientId = this.activatedRoute.snapshot.paramMap.get('clientId');
      if (this.clientId) {
        this.loadClientData(this.clientId);
      }
    }
  }

  private loadClientData(id: string): void {
    this.clientService.getClient(id).subscribe(client => {
      this.clientForm.patchValue({
        client: { name: client.name, nip: client.nip },
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

  onFormSubmit(): void {
    if (this.clientForm.valid) {
      const formValue = this.clientForm.value;
      const clientData = this.buildClientData(formValue);

      if (this.isEdit && this.clientId) {
        this.clientService.updateClient(this.clientId, clientData).subscribe(() => {
          this.router.navigate(['/clients']);
        });
      } else {
        this.clientService.addClient(clientData).subscribe(() => {
          this.router.navigate(['/clients']);
        });
      }
    }
  }

  private buildClientData(formValue: any): CreateOrUpdateClientInterface {
    return {
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
  }
}
