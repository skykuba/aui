package org.example.aui.core.dto.invoice;

import lombok.*;
import org.example.aui.core.dto.client.ClientDetailDTO;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailDTO {
    private UUID uuid;
    private String invoiceId;
    private Double netAmount;
    private LocalDateTime dateTime;
    private Boolean paid;
    private ClientDetailDTO client;
    private ClientDetailDTO issuer;
}

