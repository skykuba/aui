package org.example.aui.core.dto.invoice;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceCreateUpdateDTO {
    private String invoiceId;
    private Double netAmount;
    private Boolean paid;
    private UUID clientUuid;
    private UUID issuerUuid;
}

