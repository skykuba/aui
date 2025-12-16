package org.example.accountsofficeinvoice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInvoiceDTO {
    private String invoiceId;
    private Double netAmount;
    private Boolean paid;
}

