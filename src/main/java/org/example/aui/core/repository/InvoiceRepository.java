package org.example.aui.core.repository;

import org.example.aui.core.entity.Client;
import org.example.aui.core.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    List<Invoice> findAllByClientAndPaid(Client client, Boolean paid);
    Boolean existsByIssuerAndInvoiceId(Client issuer, String invoiceId);
}
