package org.example.aui.core.controller;

import org.example.aui.core.dto.invoice.InvoiceCreateUpdateDTO;
import org.example.aui.core.dto.invoice.InvoiceDTO;
import org.example.aui.core.dto.invoice.InvoiceDetailDTO;
import org.example.aui.core.mapper.InvoiceMapper;
import org.example.aui.core.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/")
    public List<InvoiceDTO> getAllInvoices() {
        return invoiceService.findAll().stream()
                .map(InvoiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{invoiceId}")
    public InvoiceDetailDTO getInvoiceDetail(@PathVariable UUID invoiceId) {
        return invoiceService.findById(invoiceId)
                .map(InvoiceMapper::toDetailDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));
    }

    @GetMapping("/client/{clientId}")
    public List<InvoiceDTO> getInvoicesByClient(@PathVariable UUID clientId) {
        try {
            List<InvoiceDTO> invoices = invoiceService.findAllByClientId(clientId).stream()
                    .map(InvoiceMapper::toDTO)
                    .collect(Collectors.toList());

            if (invoices.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No invoices found for this client");
            }
            return invoices;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceDetailDTO createInvoice(@RequestBody InvoiceCreateUpdateDTO dto) {
        return InvoiceMapper.toDetailDTO(invoiceService.saveFromDTO(dto));
    }

    @PutMapping("/{invoiceId}")
    public InvoiceDetailDTO updateInvoice(@PathVariable UUID invoiceId, @RequestBody InvoiceCreateUpdateDTO dto) {
        return InvoiceMapper.toDetailDTO(invoiceService.updateFromDTO(invoiceId, dto));
    }

    @DeleteMapping("/{invoiceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInvoice(@PathVariable UUID invoiceId) {
        invoiceService.deleteById(invoiceId);
    }
}
