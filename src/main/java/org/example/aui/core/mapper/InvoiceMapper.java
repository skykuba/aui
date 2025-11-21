package org.example.aui.core.mapper;

import org.example.aui.core.dto.invoice.InvoiceCreateUpdateDTO;
import org.example.aui.core.dto.invoice.InvoiceDTO;
import org.example.aui.core.dto.invoice.InvoiceDetailDTO;
import org.example.aui.core.entity.Client;
import org.example.aui.core.entity.Invoice;

public class InvoiceMapper {
    public static InvoiceDTO toDTO(Invoice invoice) {
        if (invoice == null) {
            return null;
        }
        return InvoiceDTO.builder()
                .uuid(invoice.getUuid())
                .invoiceId(invoice.getInvoiceId())
                .netAmount(invoice.getNetAmount())
                .dateTime(invoice.getDateTime())
                .paid(invoice.getPaid())
                .client(ClientMapper.toDTO(invoice.getClient()))
                .issuer(ClientMapper.toDTO(invoice.getIssuer()))
                .build();
    }

    public static InvoiceDetailDTO toDetailDTO(Invoice invoice) {
        if (invoice == null) {
            return null;
        }
        return InvoiceDetailDTO.builder()
                .uuid(invoice.getUuid())
                .invoiceId(invoice.getInvoiceId())
                .netAmount(invoice.getNetAmount())
                .dateTime(invoice.getDateTime())
                .paid(invoice.getPaid())
                .client(ClientMapper.toDetailDTO(invoice.getClient()))
                .issuer(ClientMapper.toDetailDTO(invoice.getIssuer()))
                .build();
    }

    public static Invoice toEntity(InvoiceCreateUpdateDTO dto) {
        if (dto == null) {
            return null;
        }
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(dto.getInvoiceId());
        invoice.setNetAmount(dto.getNetAmount());
        invoice.setPaid(dto.getPaid());

        if (dto.getClientUuid() != null) {
            Client client = new Client();
            client.setId(dto.getClientUuid());
            invoice.setClient(client);
        }

        if (dto.getIssuerUuid() != null) {
            Client issuer = new Client();
            issuer.setId(dto.getIssuerUuid());
            invoice.setIssuer(issuer);
        }

        return invoice;
    }
}

