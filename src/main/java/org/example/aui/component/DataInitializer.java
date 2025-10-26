package org.example.aui.component;

import org.example.aui.entity.Client;
import org.example.aui.entity.Invoice;
import org.example.aui.service.ClientService;
import org.example.aui.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.swing.*;
import java.util.*;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private ClientService clientService;
    private InvoiceService invoiceService;
    private int numberOfClients;
    private int numberOfInvoicesPerClient;
    private List<String> clientNames = List.of(
            "Apple Inc.", "Microsoft Corporation", "Amazon.com, Inc.", "Alphabet Inc.",
            "Meta Platforms, Inc.", "Tesla, Inc.", "NVIDIA Corporation",
            "Oracle Corporation", "Adobe Inc.", "Netflix, Inc."
    );

    @Autowired
    public DataInitializer(ClientService clientService,InvoiceService invoiceService) {
        this.clientService = clientService;
        this.invoiceService = invoiceService;
        this.numberOfClients = 5;
        this.numberOfInvoicesPerClient = 10;
    }

    public DataInitializer(ClientService clientService,InvoiceService invoiceService, int numberOfClients,int numberOfInvoicesPerClient) {
        this.clientService = clientService;
        this.invoiceService = invoiceService;
        this.numberOfClients = numberOfClients;
        this.numberOfInvoicesPerClient = numberOfInvoicesPerClient;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if(clientService.findAll().isEmpty()) {
            List<Client> clients = createClients();
            clients.forEach(clientService::save);
            createAndSaveInvoices();
        }
    }

    private List<Client> createClients() {
        List<Client> clients = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < Math.min(this.numberOfClients, this.clientNames.size()); i++) {
            Client client = new Client();
            String name = this.clientNames.get(i);
            String emailPrefix = name.toLowerCase().replaceAll("[^a-z0-9]", "");

            client.setId(UUID.randomUUID());
            client.setName(name);
            client.setEmail(emailPrefix + "@" +emailPrefix+".com");

            StringBuilder nip = new StringBuilder();
            for (int j = 0; j < 10; j++) {
                nip.append(random.nextInt(10));
            }
            client.setNip(nip.toString());
            client.setAddress("ul. Krzemowa " + (i + 1) + ", Dolina Krzemowa");
            clients.add(client);
        }
        return clients;
    }

    private void createAndSaveInvoices() {
        Random random = new Random();
        List<Client> clients = this.clientService.findAll();
        if (clients.size() < 2) {
            return;
        }

        for (Client issuer : clients) {
            for (int i = 0 ; i < this.numberOfInvoicesPerClient; i++) {
                Client client;
                do {
                    client = clients.get(random.nextInt(clients.size()));
                } while (client.equals(issuer));
                Invoice invoice = new Invoice();
                invoice.setUuid(UUID.randomUUID());
                invoice.setInvoiceId("FV/"+(i+1)+"/"+issuer.getName().substring(0,4).toUpperCase());
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -random.nextInt(50));
                invoice.setDate(calendar.getTime());
                invoice.setNetAmount(1000.00 + (1001.00 - 100.00 * random.nextInt(10))*random.nextInt(10));
                invoice.setPaid(random.nextBoolean());
                invoice.setClient(client);
                invoice.setIssuer(issuer);

                invoiceService.save(invoice);
            }
        }
    }
}


