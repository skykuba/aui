package org.example.aui.repository;

import org.example.aui.entity.Client;
import org.example.aui.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    List<Invoice> findAllByClientAndPaid(Client client, Boolean paid);
}
