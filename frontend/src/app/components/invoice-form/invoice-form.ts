import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { InvoiceService } from '../../services/invoice/invoice';
import { ClientService } from '../../services/client/client';
import { ActivatedRoute, Router } from '@angular/router';
import { CreateOrUpdateInvoiceInterface } from '../../models/invoice.model';
import { ClientInterface } from '../../models/client.model';
import { CommonModule } from '@angular/common';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-invoice-form',
  imports: [ReactiveFormsModule, FormsModule, CommonModule],
  templateUrl: './invoice-form.html',
  styleUrl: './invoice-form.css',
  standalone: true,
})
export class InvoiceForm implements OnInit, OnDestroy {
  invoiceForm: FormGroup;
  isEdit: boolean = false;
  clientId: string | null = '';
  invoiceId: string | null = '';
  clients: ClientInterface[] = [];
  private destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private invoiceService: InvoiceService,
    private clientService: ClientService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {
    this.invoiceForm = this.fb.group({
      invoiceId: ['', Validators.required],
      netAmount: ['', [Validators.required, Validators.min(0)]],
      paid: [false],
      clientUuid: ['', Validators.required],
      issuerUuid: ['', Validators.required],
    }, { validators: this.differentClientAndIssuerValidator });
  }

  // Custom validator - klient i wystawca nie mogą być tą samą osobą
  private differentClientAndIssuerValidator(control: AbstractControl): ValidationErrors | null {
    const clientUuid = control.get('clientUuid')?.value;
    const issuerUuid = control.get('issuerUuid')?.value;

    if (clientUuid && issuerUuid && clientUuid === issuerUuid) {
      return { sameClientAndIssuer: true };
    }

    return null;
  }

  ngOnInit() {
    // Pobierz clientId z URL - to będzie wystawca (issuer)
    this.clientId = this.activatedRoute.snapshot.paramMap.get('clientId');

    // Pobierz listę klientów
    this.clientService.getClients().subscribe(response => {
      // Backend zwraca tablicę bezpośrednio lub obiekt z polem clients
      let clientData = response.clients || response;

      if (Array.isArray(clientData)) {
        this.clients = clientData;

        // Jeśli mamy clientId z URL, ustaw go jako issuerUuid (wystawca)
        if (this.clientId) {
          this.invoiceForm.patchValue({
            issuerUuid: this.clientId
          });
        }
      } else {
        console.error('Unexpected response format:', response);
        this.clients = [];
      }
      console.log('Loaded clients:', this.clients);
    });

    const urlSegments = this.router.url.split('/');
    this.isEdit = urlSegments.includes('edit') && urlSegments.length > 2;

    if (this.isEdit) {
      this.invoiceId = this.activatedRoute.snapshot.paramMap.get('invoiceId');
      if (this.clientId && this.invoiceId) {
        this.loadInvoiceData(this.clientId, this.invoiceId);
      }
    }

    // Obserwuj zmiany pola issuerUuid i blokuj je po wypełnieniu
    this.invoiceForm.get('issuerUuid')?.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(value => {
        const issuerControl = this.invoiceForm.get('issuerUuid');
        if (value && !this.isEdit) {
          // Jeśli pole ma wartość i to nie jest edycja, wyłącz pole
          issuerControl?.disable({ emitEvent: false });
        }
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadInvoiceData(clientId: string, invoiceId: string) {
    this.invoiceService.getClientsInvoice(clientId, invoiceId).subscribe(invoice => {
      this.invoiceForm.patchValue({
        invoiceId: invoice.invoiceId,
        netAmount: invoice.netAmount,
        paid: invoice.paid,
        clientUuid: invoice.clientUuid,
        issuerUuid: invoice.issuerUuid,
      });
    });
  }

  onFormSubmit() {
    if (this.invoiceForm.valid && this.clientId) {
      const formValue = this.invoiceForm.value;
      const invoice: CreateOrUpdateInvoiceInterface = {
        invoiceId: formValue.invoiceId,
        netAmount: formValue.netAmount,
        paid: formValue.paid,
        clientUuid: formValue.clientUuid,
        issuerUuid: formValue.issuerUuid,
      };

      if (this.isEdit && this.invoiceId) {
        // Edycja - wysyłamy PUT na /api/invoices/{uuid}
        this.invoiceService.updateInvoice(this.clientId, this.invoiceId, invoice).subscribe(() => {
          this.router.navigate(['/clients', this.clientId, 'invoices']);
        });
      } else {
        // Tworzenie - wysyłamy POST na /api/invoices/client/{clientId}
        this.invoiceService.createInvoice(this.clientId, invoice).subscribe(() => {
          this.router.navigate(['/clients', this.clientId, 'invoices']);
        });
      }
    }
  }
}

