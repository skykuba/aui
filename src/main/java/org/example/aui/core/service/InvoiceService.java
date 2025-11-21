package org.example.aui.core.service;

import org.example.aui.core.dto.invoice.InvoiceCreateUpdateDTO;
import org.example.aui.core.entity.Client;
import org.example.aui.core.entity.Invoice;
import org.example.aui.core.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceService {
    private final InvoiceRepository repository;
    private final ClientService clientService;

    public InvoiceService(InvoiceRepository repository, ClientService clientService) {
        this.repository = repository;
        this.clientService = clientService;
    }

    @Transactional(readOnly = true)
    public Optional<Invoice> findById(UUID uuid){
        return repository.findById(uuid);
    }

    @Transactional(readOnly = true)
    public List<Invoice> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void save(Invoice invoice) {
        if (!repository.existsByIssuerAndInvoiceId(invoice.getIssuer(), invoice.getInvoiceId())){
            repository.save(invoice);
        }
        else {
            throw new IllegalArgumentException("Invoice with this number " + invoice.getInvoiceId() + " already exists");
        }
    }

    @Transactional
    public void deleteById(UUID uuid) {
        repository.deleteById(uuid);
    }

    @Transactional
    public void deleteAllByClient(Client client) {
        List<Invoice> invoices = repository.findAllByClientOrIssuer(client, client);
        repository.deleteAll(invoices);
    }

    @Transactional(readOnly = true)
    public List<Invoice> findAllByClientAndPaid(Client client,boolean paid) {
        return repository.findAllByClientAndPaid(client,paid);
    }

    @Transactional(readOnly = true)
    public List<Invoice> findAllByClientId(UUID clientId) {
        Client client = clientService.getOrThrow(clientId);
        return repository.findAllByClient(client);
    }

    @Transactional
    public Invoice saveFromDTO(InvoiceCreateUpdateDTO invoiceDTO) {
        if (invoiceDTO == null) {
            throw new IllegalArgumentException("Invoice data cannot be null");
        }

        if (invoiceDTO.getInvoiceId() == null || invoiceDTO.getInvoiceId().trim().isEmpty()) {
            throw new IllegalArgumentException("Invoice ID cannot be null or empty");
        }

        if (invoiceDTO.getClientUuid() == null) {
            throw new IllegalArgumentException("Client is required");
        }

        if (invoiceDTO.getIssuerUuid() == null) {
            throw new IllegalArgumentException("Issuer is required");
        }

        Client client = clientService.getOrThrow(invoiceDTO.getClientUuid());
        Client issuer = clientService.getOrThrow(invoiceDTO.getIssuerUuid());

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceDTO.getInvoiceId());
        invoice.setNetAmount(invoiceDTO.getNetAmount());
        invoice.setPaid(invoiceDTO.getPaid());
        invoice.setClient(client);
        invoice.setIssuer(issuer);

        save(invoice);

        return invoice;
    }

    @Transactional
    public Invoice updateFromDTO(UUID uuid, InvoiceCreateUpdateDTO invoiceDTO) {
        Invoice existingInvoice = findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        if (invoiceDTO == null) {
            throw new IllegalArgumentException("Invoice data cannot be null");
        }

        if (invoiceDTO.getInvoiceId() == null || invoiceDTO.getInvoiceId().trim().isEmpty()) {
            throw new IllegalArgumentException("Invoice ID cannot be null or empty");
        }

        if (invoiceDTO.getClientUuid() == null) {
            throw new IllegalArgumentException("Client is required");
        }

        if (invoiceDTO.getIssuerUuid() == null) {
            throw new IllegalArgumentException("Issuer is required");
        }

        Client client = clientService.getOrThrow(invoiceDTO.getClientUuid());
        Client issuer = clientService.getOrThrow(invoiceDTO.getIssuerUuid());

        // Check if invoiceId or issuer changed and validate uniqueness
        boolean invoiceIdChanged = !existingInvoice.getInvoiceId().equals(invoiceDTO.getInvoiceId());
        boolean issuerChanged = !existingInvoice.getIssuer().getId().equals(issuer.getId());

        if (invoiceIdChanged || issuerChanged) {
            if (repository.existsByIssuerAndInvoiceId(issuer, invoiceDTO.getInvoiceId())) {
                throw new IllegalArgumentException("Invoice with this number " + invoiceDTO.getInvoiceId() + " already exists for this issuer");
            }
        }

        existingInvoice.setInvoiceId(invoiceDTO.getInvoiceId());
        existingInvoice.setNetAmount(invoiceDTO.getNetAmount());
        existingInvoice.setPaid(invoiceDTO.getPaid());
        existingInvoice.setClient(client);
        existingInvoice.setIssuer(issuer);

        repository.save(existingInvoice);

        return existingInvoice;
    }
}
