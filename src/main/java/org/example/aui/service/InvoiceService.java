package org.example.aui.service;

import org.example.aui.entity.Client;
import org.example.aui.entity.Invoice;
import org.example.aui.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceService {
    private final InvoiceRepository repository;

    public InvoiceService(InvoiceRepository repository) {
        this.repository = repository;
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
    public Invoice save(Invoice invoice) {
        if (!repository.existsByIssuerAndInvoiceId(invoice.getIssuer(), invoice.getInvoiceId())){
            return repository.save(invoice);
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
    public List<Invoice> findAllByClientAndPaid(Client client,boolean paid) {
        return repository.findAllByClientAndPaid(client,paid);
    }
}
