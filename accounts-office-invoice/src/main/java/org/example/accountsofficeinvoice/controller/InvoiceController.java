package org.example.accountsofficeinvoice.controller;

import org.example.accountsofficeinvoice.dto.CreateInvoiceDTO;
import org.example.accountsofficeinvoice.dto.InvoiceDTO;
import org.example.accountsofficeinvoice.dto.UpdateInvoiceDTO;
import org.example.accountsofficeinvoice.entity.Invoice;
import org.example.accountsofficeinvoice.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }


    @PostMapping("/")
    public ResponseEntity<InvoiceDTO> createInvoice(@RequestBody CreateInvoiceDTO createInvoiceDTO) {
        try {
            Invoice invoice = convertCreateDTOToEntity(createInvoiceDTO);
            Invoice createdInvoice = invoiceService.createInvoice(invoice);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertEntityToDTO(createdInvoice));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Pobierz fakturę po UUID
     * GET /api/invoices/{uuid}
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable UUID uuid) {
        Optional<Invoice> invoice = invoiceService.getInvoiceById(uuid);
        return invoice.map(inv -> ResponseEntity.ok(convertEntityToDTO(inv)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Pobierz wszystkie faktury
     * GET /api/invoices
     */
    @GetMapping("/")
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        List<InvoiceDTO> invoiceDTOs = invoices.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(invoiceDTOs);
    }

    /**
     * Aktualizuj fakturę
     * PUT /api/invoices/{uuid}
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<InvoiceDTO> updateInvoice(
            @PathVariable UUID uuid,
            @RequestBody UpdateInvoiceDTO updateInvoiceDTO) {
        try {
            Invoice invoiceDetails = convertUpdateDTOToEntity(updateInvoiceDTO);
            Invoice updatedInvoice = invoiceService.updateInvoice(uuid, invoiceDetails);
            return ResponseEntity.ok(convertEntityToDTO(updatedInvoice));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Usuń fakturę
     * DELETE /api/invoices/{uuid}
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable UUID uuid) {
        try {
            invoiceService.deleteInvoice(uuid);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Pobierz wszystkie faktury dla danego klienta
     * GET /api/invoices/client/{clientUuid}
     */
    @GetMapping("/client/{clientUuid}")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByClient(@PathVariable UUID clientUuid) {
        List<Invoice> invoices = invoiceService.getInvoicesByClient(clientUuid);
        List<InvoiceDTO> invoiceDTOs = invoices.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(invoiceDTOs);
    }

    @GetMapping("/client/{clientUuid}/{invoiceUuid}")
    public ResponseEntity<InvoiceDTO> getClientsInvoiceById(@PathVariable UUID clientUuid, @PathVariable UUID invoiceUuid) {
        try {
            Invoice invoice = this.invoiceService.getInvoiceByUuidAndClient(clientUuid, invoiceUuid);
            return ResponseEntity.ok(convertEntityToDTO(invoice));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Pobierz wszystkie faktury wydane przez danego emienta
     * GET /api/invoices/issuer/{issuerUuid}
     */
    @GetMapping("/issuer/{issuerUuid}")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByIssuer(@PathVariable UUID issuerUuid) {
        List<Invoice> invoices = invoiceService.getInvoicesByIssuer(issuerUuid);
        List<InvoiceDTO> invoiceDTOs = invoices.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(invoiceDTOs);
    }

    /**
     * Pobierz faktury dla danego klienta z filtrem opłacenia
     * GET /api/invoices/client/{clientUuid}/paid?paid=true
     */
    @GetMapping("/client/{clientUuid}/paid")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByClientAndPaidStatus(
            @PathVariable UUID clientUuid,
            @RequestParam Boolean paid) {
        List<Invoice> invoices = invoiceService.getInvoicesByClientAndPaidStatus(clientUuid, paid);
        List<InvoiceDTO> invoiceDTOs = invoices.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(invoiceDTOs);
    }


    /**
     * Oznacz fakturę jako opłaconą
     * PUT /api/invoices/{uuid}/mark-paid
     */
    @PutMapping("/{uuid}/mark-paid")
    public ResponseEntity<InvoiceDTO> markInvoiceAsPaid(@PathVariable UUID uuid) {
        try {
            Invoice invoice = invoiceService.markInvoiceAsPaid(uuid);
            return ResponseEntity.ok(convertEntityToDTO(invoice));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Oznacz fakturę jako nieopłaconą
     * PUT /api/invoices/{uuid}/mark-unpaid
     */
    @PutMapping("/{uuid}/mark-unpaid")
    public ResponseEntity<InvoiceDTO> markInvoiceAsUnpaid(@PathVariable UUID uuid) {
        try {
            Invoice invoice = invoiceService.markInvoiceAsUnpaid(uuid);
            return ResponseEntity.ok(convertEntityToDTO(invoice));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Sprawdź, czy faktura o danym numerze już istnieje
     * GET /api/invoices/exists?issuerUuid={uuid}&invoiceId={id}
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> invoiceExists(
            @RequestParam UUID issuerUuid,
            @RequestParam String invoiceId) {
        Boolean exists = invoiceService.invoiceExists(issuerUuid, invoiceId);
        return ResponseEntity.ok(exists);
    }

    /**
     * Pobierz faktury powiązane z klientem lub emitentem
     * GET /api/invoices/search?clientUuid={uuid}&issuerUuid={uuid}
     */
    @GetMapping("/search")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByClientOrIssuer(
            @RequestParam UUID clientUuid,
            @RequestParam UUID issuerUuid) {
        List<Invoice> invoices = invoiceService.getInvoicesByClientOrIssuer(clientUuid, issuerUuid);
        List<InvoiceDTO> invoiceDTOs = invoices.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(invoiceDTOs);
    }

    // ...existing code...
    private Invoice convertCreateDTOToEntity(CreateInvoiceDTO dto) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(dto.getInvoiceId());
        invoice.setNetAmount(dto.getNetAmount());
        invoice.setPaid(dto.getPaid() != null ? dto.getPaid() : false);
        invoice.setClientUuid(dto.getClientUuid());
        invoice.setIssuerUuid(dto.getIssuerUuid());
        return invoice;
    }

    /**
     * Konwertuj UpdateInvoiceDTO na encję Invoice
     */
    private Invoice convertUpdateDTOToEntity(UpdateInvoiceDTO dto) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(dto.getInvoiceId());
        invoice.setNetAmount(dto.getNetAmount());
        invoice.setPaid(dto.getPaid());
        return invoice;
    }

    /**
     * Konwertuj encję Invoice na InvoiceDTO
     */
    private InvoiceDTO convertEntityToDTO(Invoice invoice) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setUuid(invoice.getUuid());
        dto.setInvoiceId(invoice.getInvoiceId());
        dto.setNetAmount(invoice.getNetAmount());
        dto.setDateTime(invoice.getDateTime());
        dto.setPaid(invoice.getPaid());
        dto.setClientUuid(invoice.getClientUuid());
        dto.setIssuerUuid(invoice.getIssuerUuid());
        return dto;
    }
}
