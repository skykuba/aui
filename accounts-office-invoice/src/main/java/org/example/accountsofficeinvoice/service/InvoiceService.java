package org.example.accountsofficeinvoice.service;

import org.example.accountsofficeinvoice.entity.Invoice;
import org.example.accountsofficeinvoice.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice createInvoice(Invoice invoice) {
        if (invoiceRepository.existsByIssuerUuidAndInvoiceId(invoice.getIssuerUuid(), invoice.getInvoiceId())) {
            throw new IllegalArgumentException("Faktura o numerze " + invoice.getInvoiceId() + " już istnieje dla tego emienta");
        }
        return invoiceRepository.save(invoice);
    }


    public Optional<Invoice> getInvoiceById(UUID uuid) {
        return invoiceRepository.findById(uuid);
    }


    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }


    public Invoice updateInvoice(UUID uuid, Invoice invoiceDetails) {
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(uuid);

        if (invoiceOptional.isPresent()) {
            Invoice invoice = invoiceOptional.get();

            if (invoiceDetails.getNetAmount() != null) {
                invoice.setNetAmount(invoiceDetails.getNetAmount());
            }
            if (invoiceDetails.getPaid() != null) {
                invoice.setPaid(invoiceDetails.getPaid());
            }

            return invoiceRepository.save(invoice);
        }

        throw new IllegalArgumentException("Faktura o UUID " + uuid + " nie znaleziona");
    }


    public void deleteInvoice(UUID uuid) {
        if (!invoiceRepository.existsById(uuid)) {
            throw new IllegalArgumentException("Faktura o UUID " + uuid + " nie znaleziona");
        }
        invoiceRepository.deleteById(uuid);
    }


    public List<Invoice> getInvoicesByClient(UUID clientUuid) {
        return invoiceRepository.findAllByClientUuid(clientUuid);
    }


    public List<Invoice> getInvoicesByIssuer(UUID issuerUuid) {
        return invoiceRepository.findAllByIssuerUuid(issuerUuid);
    }


    public List<Invoice> getInvoicesByClientAndPaidStatus(UUID clientUuid, Boolean paid) {
        return invoiceRepository.findAllByClientUuidAndPaid(clientUuid, paid);
    }


    public List<Invoice> getInvoicesByClientOrIssuer(UUID clientUuid, UUID issuerUuid) {
        return invoiceRepository.findAllByClientOrIssuer(clientUuid, issuerUuid);
    }

    public Invoice getInvoiceByUuidAndClient(UUID clientUuid, UUID invoiceUuid) {
        Invoice invoice =  this.invoiceRepository.getInvoiceByUuidAndClientUuid(invoiceUuid, clientUuid);
        if (invoice == null) {
            throw new IllegalArgumentException("Faktura o UUID " + invoiceUuid + " nie znaleziona dla klienta " + clientUuid);
        }
        return invoice;
    }


    public Invoice markInvoiceAsPaid(UUID uuid) {
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(uuid);

        if (invoiceOptional.isPresent()) {
            Invoice invoice = invoiceOptional.get();
            invoice.setPaid(true);
            return invoiceRepository.save(invoice);
        }

        throw new IllegalArgumentException("Faktura o UUID " + uuid + " nie znaleziona");
    }


    public Invoice markInvoiceAsUnpaid(UUID uuid) {
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(uuid);

        if (invoiceOptional.isPresent()) {
            Invoice invoice = invoiceOptional.get();
            invoice.setPaid(false);
            return invoiceRepository.save(invoice);
        }

        throw new IllegalArgumentException("Faktura o UUID " + uuid + " nie znaleziona");
    }


    public Boolean invoiceExists(UUID issuerUuid, String invoiceId) {
        return invoiceRepository.existsByIssuerUuidAndInvoiceId(issuerUuid, invoiceId);
    }
}
