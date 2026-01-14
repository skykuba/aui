import { Component, OnInit, OnDestroy, Input, OnChanges, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { InvoiceService } from '../../services/invoice/invoice';
import { ClientService } from '../../services/client/client';
import { ActivatedRoute, Router } from '@angular/router';
import { CreateOrUpdateInvoiceInterface, InvoiceInterface } from '../../models/invoice.model';
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
export class InvoiceForm implements OnInit, OnDestroy, OnChanges {
  @Input() invoiceData: InvoiceInterface | null = null;
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

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['invoiceData'] && changes['invoiceData'].currentValue) {
      this.isEdit = true;
      const invoice = changes['invoiceData'].currentValue;
      this.invoiceId = invoice.invoiceId;
      this.loadInvoiceData(invoice);
    }
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
    const urlSegments = this.router.url.split('/');
    this.isEdit = this.isEdit || urlSegments.includes('edit'); // Preserve isEdit from ngOnChanges
    this.clientId = this.activatedRoute.snapshot.paramMap.get('clientId');

    // Pobierz listę klientów
    this.clientService.getClients().subscribe(response => {
      let clientData = response.clients || response;

      if (Array.isArray(clientData)) {
        this.clients = clientData;

        // Ustaw wystawcę (issuer) tylko podczas tworzenia nowej faktury
        if (!this.isEdit && this.clientId) {
          this.invoiceForm.patchValue({
            issuerUuid: this.clientId
          });
        }
      } else {
        console.error('Unexpected response format:', response);
        this.clients = [];
      }
    });

    if (this.isEdit && !this.invoiceData) {
      this.invoiceId = this.activatedRoute.snapshot.paramMap.get('invoiceId');
      if (this.clientId && this.invoiceId) {
        this.invoiceService.getClientsInvoice(this.clientId, this.invoiceId).subscribe(invoice => {
          this.loadInvoiceData(invoice);
        });
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

  private loadInvoiceData(invoice: InvoiceInterface) {
    this.invoiceForm.patchValue({
      invoiceId: invoice.invoiceId,
      netAmount: invoice.netAmount,
      paid: invoice.paid,
      clientUuid: invoice.clientUuid,
      issuerUuid: invoice.issuerUuid,
    });
    // Blokuj pola, które nie powinny być edytowane
    this.invoiceForm.get('invoiceId')?.disable();
    this.invoiceForm.get('clientUuid')?.disable();
    this.invoiceForm.get('issuerUuid')?.disable();
  }

  public getClientName(clientId: string): string {
    if (!clientId || !this.clients) return '';
    const client = this.clients.find(c => c.id === clientId);
    return client ? client.name : '';
  }

  onFormSubmit() {
    if (this.invoiceForm.valid && this.clientId) {
      const formValue = this.invoiceForm.getRawValue(); // Użyj getRawValue(), aby pobrać wartość z wyłączonych pól
      const invoice: CreateOrUpdateInvoiceInterface = {
        invoiceId: formValue.invoiceId,
        netAmount: formValue.netAmount,
        paid: formValue.paid,
        clientUuid: formValue.clientUuid,
        issuerUuid: formValue.issuerUuid,
      };

      if (this.isEdit && this.invoiceId) {
        this.invoiceService.updateInvoice(this.clientId, this.invoiceId, invoice).subscribe(() => {
          this.router.navigate(['/clients', this.clientId, 'invoices']);
        });
      } else {
        this.invoiceService.createInvoice(this.clientId, invoice).subscribe(() => {
          this.router.navigate(['/clients', this.clientId, 'invoices']);
        });
      }
    }
  }
}
