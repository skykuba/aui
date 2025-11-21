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
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private final ClientService clientService;
    private final InvoiceService invoiceService;
    private final CityRepository cityRepository;
    private final AddressRepository addressRepository;
    private int numberOfClients;
    private int numberOfInvoicesPerClient;
    private List<String> clientNames = List.of(
            "Apple Inc.", "Microsoft Corporation", "Amazon.com, Inc.", "Alphabet Inc.",
            "Meta Platforms, Inc.", "Tesla, Inc.", "NVIDIA Corporation",
            "Oracle Corporation", "Adobe Inc.", "Netflix, Inc."
    );

    @Autowired
    public DataInitializer(ClientService clientService, InvoiceService invoiceService,
                          CityRepository cityRepository, AddressRepository addressRepository) {
        this.clientService = clientService;
        this.invoiceService = invoiceService;
        this.cityRepository = cityRepository;
        this.addressRepository = addressRepository;
        this.numberOfClients = 5;
        this.numberOfInvoicesPerClient = 10;
    }



    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if(clientService.findAll().isEmpty()) {
            List<City> cities = createAndSaveCities();

            List<Address> addresses = createAddresses(cities);
            addresses.forEach(addressRepository::save);

            List<Client> clients = createClients(addresses);
            clients.forEach(clientService::save);

            createAndSaveInvoices();
        }
    }

    private List<City> createAndSaveCities() {
        List<City> cities = new ArrayList<>();

        cities.add(findOrCreateCity("Wrocław", "Dolnośląskie", "Polska"));
        cities.add(findOrCreateCity("Warszawa", "Mazowieckie", "Polska"));
        cities.add(findOrCreateCity("Kraków", "Małopolskie", "Polska"));

        return cities;
    }

    private City findOrCreateCity(String cityName, String state, String country) {
        Optional<City> existingCity = cityRepository.findByCityAndStateAndCountry(cityName, state, country);

        if (existingCity.isPresent()) {
            return existingCity.get();
        }

        City newCity = new City();
        newCity.setCity(cityName);
        newCity.setState(state);
        newCity.setCountry(country);
        return cityRepository.save(newCity);
    }

    private List<Address> createAddresses(List<City> cities) {
        List<Address> addresses = new ArrayList<>();
        Random random = new Random();

        String[] streets = {"Krzemowa", "Główna", "Kwiatowa", "Leśna", "Parkowa",
                           "Słoneczna", "Kolejowa", "Wiejska", "Polna", "Szkolna"};

        for (int i = 0; i < this.numberOfClients; i++) {
            Address address = new Address();
            address.setStreet("ul. " + streets[i % streets.length]);
            address.setBuildingNumber(String.valueOf(i + 1));
            address.setCity(cities.get(i % cities.size()));
            addresses.add(address);
        }

        return addresses;
    }

    private List<Client> createClients(List<Address> addresses) {
        List<Client> clients = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < Math.min(this.numberOfClients, this.clientNames.size()); i++) {
            Client client = new Client();
            String name = this.clientNames.get(i);
            String emailPrefix = name.toLowerCase().replaceAll("[^a-z0-9]", "");

            client.setName(name);
            client.setEmail(emailPrefix + "@" + emailPrefix + ".com");

            StringBuilder nip = new StringBuilder();
            for (int j = 0; j < 10; j++) {
                nip.append(random.nextInt(10));
            }
            client.setNip(nip.toString());
            client.setAddress(addresses.get(i));
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
                invoice.setInvoiceId("FV/"+(i+1)+"/"+issuer.getName().substring(0,4).toUpperCase());
                invoice.setNetAmount(1000.00 + (1001.00 - 100.00 * random.nextInt(10))*random.nextInt(10));
                invoice.setPaid(random.nextBoolean());
                invoice.setClient(client);
                invoice.setIssuer(issuer);

                invoiceService.save(invoice);
            }
        }
    }
}


