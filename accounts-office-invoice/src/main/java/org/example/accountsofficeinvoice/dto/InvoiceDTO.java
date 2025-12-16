package org.example.accountsofficeinvoice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class InvoiceDTO {
    private UUID uuid;
    private String invoiceId;
    private Double netAmount;
    private LocalDateTime dateTime;
    private Boolean paid;
    private UUID clientUuid;
    private UUID issuerUuid;
}

