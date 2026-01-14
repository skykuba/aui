import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { InvoiceService } from '../../services/invoice/invoice';
import { ClientService } from '../../services/client/client';
import { InvoiceInterface, CreateOrUpdateInvoiceInterface } from '../../models/invoice.model';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-update-invoice',
  imports: [ReactiveFormsModule],
  templateUrl: './update-invoice.html',
  styleUrl: './update-invoice.css',
  standalone: true,
})
export class UpdateInvoice implements OnInit {
  invoice!: InvoiceInterface;
  invoiceForm!: FormGroup;
  clientName = '';
  issuerName = '';
  isLoading = true;
  isSubmitting = false;
  error: string | null = null;

  private clientId: string | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private invoiceService: InvoiceService,
    private clientService: ClientService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadInvoiceData();
  }

  private initForm(): void {
    this.invoiceForm = this.fb.group({
      netAmount: [0, [Validators.required, Validators.min(0.01)]],
      paid: [false]
    });
  }

  private loadInvoiceData(): void {
    const invoiceId = this.route.snapshot.paramMap.get('invoiceId');
    this.clientId = this.route.snapshot.paramMap.get('clientId');

    if (!invoiceId || !this.clientId) {
      this.error = 'Brak wymaganych parametrów (clientId lub invoiceId)';
      return;
    }

    this.invoiceService.getClientsInvoice(this.clientId, invoiceId).subscribe({
      next: (invoice) => {
        this.invoice = invoice;
        this.invoiceForm.patchValue({
          netAmount: invoice.netAmount,
          paid: invoice.paid
        });
        this.loadClientNames(invoice.clientUuid, invoice.issuerUuid);
      },
      error: (err) => {
        console.error('Error loading invoice:', err);
        this.error = 'Nie udało się załadować danych faktury';
        this.isLoading = false;
      }
    });
  }

  private loadClientNames(clientUuid: string, issuerUuid: string): void {
    forkJoin({
      client: this.clientService.getClient(clientUuid),
      issuer: this.clientService.getClient(issuerUuid)
    }).subscribe({
      next: ({ client, issuer }) => {
        this.clientName = client.name;
        this.issuerName = issuer.name;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading client names:', err);
        this.clientName = clientUuid;
        this.issuerName = issuerUuid;
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  onSubmit(): void {
    if (!this.invoiceForm.valid || !this.clientId || !this.invoice) {
      return;
    }

    this.isSubmitting = true;
    const formValue = this.invoiceForm.value;

    const updateData: CreateOrUpdateInvoiceInterface = {
      invoiceId: this.invoice.invoiceId,
      netAmount: formValue.netAmount,
      paid: formValue.paid,
      clientUuid: this.invoice.clientUuid,
      issuerUuid: this.invoice.issuerUuid
    };

    this.invoiceService.updateInvoice(this.clientId, this.invoice.uuid, updateData).subscribe({
      next: () => {
        this.router.navigate(['/clients', this.clientId, 'invoices']);
      },
      error: (err) => {
        console.error('Error updating invoice:', err);
        this.isSubmitting = false;
        this.error = 'Nie udało się zaktualizować faktury';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/clients', this.clientId, 'invoices']);
  }
}

