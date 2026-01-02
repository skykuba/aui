export interface InvoiceInterface {
   uuid: string;
   invoiceId: string;
   netAmount: number;
   paid: boolean;
   clientUuid: string;
   issuerUuid: string;
}

export interface CreateOrUpdateInvoiceInterface {
  invoiceId: string;
  netAmount: number;
  paid: boolean;
  clientUuid: string;
  issuerUuid: string;
}

export interface SimpleInvoiceInterface {
  uuid: String;
  invoiceId: string;
  netAmount: number;
  paid: boolean;
  clientName: string;
  clientUuid: string;
  issuerUuid: string;
  issuerName: string;
}

export interface InvoiceListResponse {
  invoices: InvoiceInterface[];
}

export class Invoice implements InvoiceInterface {
  uuid: string;
  invoiceId: string;
  netAmount: number;
  paid: boolean;
  clientUuid: string;
  issuerUuid: string;

  constructor(data: InvoiceInterface) {
    this.uuid = data.uuid;
    this.invoiceId = data.invoiceId;
    this.netAmount = data.netAmount;
    this.paid = data.paid;
    this.clientUuid = data.clientUuid;
    this.issuerUuid = data.issuerUuid;
  }

  static toSimple(invoice: Invoice): SimpleInvoiceInterface {
    return {
      uuid: invoice.uuid,
      invoiceId: invoice.invoiceId,
      netAmount: invoice.netAmount,
      paid: invoice.paid,
      clientName: '',
      clientUuid: invoice.clientUuid,
      issuerUuid: invoice.issuerUuid,
      issuerName: '',
    };
  }

  toString(): string {
    return `Invoice(uuid: ${this.uuid}, invoiceId: ${this.invoiceId}, netAmount: ${this.netAmount}, paid: ${this.paid}, clientUuid: ${this.clientUuid}, issuerUuid: ${this.issuerUuid})`;
  }
}
