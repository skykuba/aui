package org.example.accountsofficeinvoice.component;

import org.example.accountsofficeinvoice.entity.Invoice;
import org.example.accountsofficeinvoice.entity.SimpleClient;
import org.example.accountsofficeinvoice.service.InvoiceService;
import org.example.accountsofficeinvoice.service.SimpleClientService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer {

    private final SimpleClientService simpleClientService;
    private final InvoiceService invoiceService;

    private int clientCount = 0;
    private static final int MIN_CLIENTS = 5;
    private boolean initialized = false;

    public DataInitializer(SimpleClientService simpleClientService, InvoiceService invoiceService) {
        this.simpleClientService = simpleClientService;
        this.invoiceService = invoiceService;
    }

    @EventListener
    public void handleClientCreated(ClientCreatedEvent event) {
        if (!initialized) {
            clientCount++;
            if (clientCount >= MIN_CLIENTS) {
                initializeData();
                initialized = true;
            }
        }
    }

    private void initializeData() {
        System.out.println("Inicjalizacja danych po otrzymaniu " + MIN_CLIENTS + " klientów.");

        List<SimpleClient> clients = simpleClientService.getAllClients();
        int invoiceCount = 1;

        for (int i = 0; i < clients.size(); i++) {
            for (int j = 0; j < clients.size(); j++) {
                if (i != j) {
                    for (int k = 1; k <= 5; k++) {
                        Invoice invoice = new Invoice();
                        invoice.setIssuerUuid(clients.get(i).getId());
                        invoice.setClientUuid(clients.get(j).getId());
                        invoice.setInvoiceId("INV-" + invoiceCount++);
                        invoice.setNetAmount(Math.random() * 1000 + 100);
                        invoice.setPaid(false);
                        invoiceService.createInvoice(invoice);
                    }
                }
            }
        }

        System.out.println("Utworzono faktury między klientami.");
    }
}
