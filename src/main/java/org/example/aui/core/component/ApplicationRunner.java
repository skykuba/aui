package org.example.aui.core.component;

import org.example.aui.core.entity.Address;
import org.example.aui.core.entity.City;
import org.example.aui.core.entity.Client;
import org.example.aui.core.entity.Invoice;
import org.example.aui.core.repository.AddressRepository;
import org.example.aui.core.repository.CityRepository;
import org.example.aui.core.service.ClientService;
import org.example.aui.core.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Order(2)
public class ApplicationRunner implements CommandLineRunner {

    private final ClientService clientService;
    private final InvoiceService invoiceService;
    private final CityRepository cityRepository;
    private final AddressRepository addressRepository;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public ApplicationRunner(ClientService clientService, InvoiceService invoiceService,
                           CityRepository cityRepository, AddressRepository addressRepository) {
        this.clientService = clientService;
        this.invoiceService = invoiceService;
        this.cityRepository = cityRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Welcome to the Invoice Management Application!");
        displayCommands();

        String command;
        while (true) {
            System.out.print("Enter command: ");
            command = scanner.nextLine();

            switch (command.toLowerCase()) {
                case "help":
                    displayCommands();
                    break;
                case "list_clients":
                    listClients();
                    break;
                case "add_client":
                    addClient();
                    break;
                case "list_invoices":
                    listInvoices();
                    break;
                case "add_invoice":
                    addInvoice();
                    break;
                case "delete_invoice":
                    deleteInvoice();
                    break;
                case "list_unpaid":
                    listClientsWithUnpaidInvoices();
                    break;
                case "exit":
                    System.out.println("Exiting application.");
                    return;
                default:
                    System.out.println("Unknown command. Type 'help' to see the list of available commands.");
                    break;
            }
        }
    }

    private void displayCommands() {
        System.out.println("Available commands:");
        System.out.println("  help - displays this list");
        System.out.println("  list_clients - displays all clients");
        System.out.println("  add_client - adds a new client");
        System.out.println("  list_invoices - displays all invoices");
        System.out.println("  add_invoice - adds a new invoice");
        System.out.println("  delete_invoice - deletes an existing invoice");
        System.out.println("  list_unpaid - displays clients with unpaid invoices for a given issuer");
        System.out.println("  exit - closes the application");
    }

    private void listClients() {
        List<Client> clients = clientService.findAllWithAddressAndCity();
        if (clients.isEmpty()) {
            System.out.println("No clients in the database.");
            return;
        }
        System.out.println("Client list:");
        clients.forEach(c -> System.out.printf("  ID: %s, Name: %s, NIP: %s, City: %s, Country: %s%n",
                c.getId(), c.getName(), c.getNip(),
                c.getAddress().getCity().getCity(),
                c.getAddress().getCity().getCountry()));
    }

    private void addClient() {
        try {
            System.out.println("Adding a new client.");

            System.out.print("Enter client name: ");
            String name = scanner.nextLine();

            System.out.print("Enter client NIP: ");
            String nip = scanner.nextLine();
            if (clientService.findClientByNip(nip).isPresent()) {
                System.out.println("Error: A client with this NIP already exists.");
                return;
            }

            System.out.print("Enter client email: ");
            String email = scanner.nextLine();

            // Tworzenie miasta
            System.out.print("Enter city name: ");
            String cityName = scanner.nextLine();

            System.out.print("Enter state: ");
            String state = scanner.nextLine();

            System.out.print("Enter country: ");
            String country = scanner.nextLine();

            City city = new City();
            city.setCity(cityName);
            city.setState(state);
            city.setCountry(country);
            cityRepository.save(city);

            // Tworzenie adresu
            System.out.print("Enter street name: ");
            String street = scanner.nextLine();

            System.out.print("Enter building number: ");
            String buildingNumber = scanner.nextLine();

            Address address = new Address();
            address.setStreet(street);
            address.setBuildingNumber(buildingNumber);
            address.setCity(city);
            addressRepository.save(address);

            Client newClient = new Client();
            newClient.setName(name);
            newClient.setNip(nip);
            newClient.setEmail(email);
            newClient.setAddress(address);

            clientService.save(newClient);
            System.out.println("Successfully added a new client with ID: " + newClient.getId());

        } catch (Exception e) {
            System.out.println("An unexpected error occurred. Please check the entered data.");
        }
    }

    private void listInvoices() {
        List<Invoice> invoices = invoiceService.findAll();
        if (invoices.isEmpty()) {
            System.out.println("No invoices in the database.");
            return;
        }
        System.out.println("Invoice list:");
        invoices.forEach(i -> System.out.printf("  ID: %s, Number: %s, Issuer: %s, Client: %s, Amount: %.2f, Paid: %b%n",
                i.getUuid(), i.getInvoiceId(), i.getIssuer().getName(), i.getClient().getName(), i.getNetAmount(), i.getPaid()));
    }

    private void addInvoice() {
        try {
            System.out.println("Adding a new invoice.");
            listClients();
            List<Client> clients = clientService.findAll();
            if (clients.size() < 2) {
                System.out.println("There must be at least two clients to issue an invoice.");
                return;
            }

            System.out.print("Enter issuer NIP (copy from the list above): ");
            String issuerNip = scanner.nextLine();
            Client issuer = clientService.findClientByNip(issuerNip).orElseThrow(() -> new IllegalArgumentException("Issuer with the given NIP not found."));

            System.out.print("Enter client NIP (copy from the list above): ");
            String clientNip = scanner.nextLine();
            Client client = clientService.findClientByNip(clientNip).orElseThrow(() -> new IllegalArgumentException("Client with the given NIP not found."));

            if (issuer.equals(client)) {
                System.out.println("Issuer cannot be the same as the client.");
                return;
            }

            System.out.print("Enter invoice number (e.g., FV/1/2024): ");
            String invoiceId = scanner.nextLine();

            System.out.print("Enter net amount: ");
            double netAmount = Double.parseDouble(scanner.nextLine());

            Invoice newInvoice = new Invoice();
            newInvoice.setInvoiceId(invoiceId);
            newInvoice.setNetAmount(netAmount);
            newInvoice.setPaid(false);
            newInvoice.setIssuer(issuer);
            newInvoice.setClient(client);

            invoiceService.save(newInvoice);
            System.out.println("Successfully added a new invoice with ID: " + newInvoice.getUuid());

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred. Please check the entered data format.");
        }
    }

    private void deleteInvoice() {
        try {
            System.out.println("Deleting an invoice.");
            listInvoices();
            System.out.print("Enter the ID of the invoice to delete (copy from the list above): ");
            UUID invoiceUuid = UUID.fromString(scanner.nextLine());

            if (invoiceService.findById(invoiceUuid).isPresent()) {
                invoiceService.deleteById(invoiceUuid);
                System.out.println("Successfully deleted the invoice.");
            } else {
                System.out.println("Invoice with the given ID not found.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid ID format.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred.");
        }
    }

    private void listClientsWithUnpaidInvoices() {
        try {
            System.out.println("Displaying clients with unpaid invoices.");
            System.out.println("Available issuers:");
            listClients();
            System.out.print("Enter the issuer ID to find debtors for: ");
            UUID issuerId = UUID.fromString(scanner.nextLine());
            Client issuer = clientService.findById(issuerId)
                    .orElseThrow(() -> new IllegalArgumentException("Issuer with the given ID not found."));

            List<Client> clientsWithUnpaidInvoices = clientService.findAllClientsWithUnpaidInvoices(issuer);

            if (clientsWithUnpaidInvoices.isEmpty()) {
                System.out.printf("No clients with unpaid invoices found for issuer: %s%n", issuer.getName());
                return;
            }

            System.out.printf("Clients with unpaid invoices for issuer '%s':%n", issuer.getName());
            clientsWithUnpaidInvoices.forEach(c -> System.out.printf("  ID: %s, Name: %s, NIP: %s%n", c.getId(), c.getName(), c.getNip()));

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred.");
        }
    }
}
