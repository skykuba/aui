package org.example.accountsofficeinvoice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateInvoiceDTO {
    private String invoiceId;
    private Double netAmount;
    private Boolean paid;
    private UUID clientUuid;
    private UUID issuerUuid;
}

