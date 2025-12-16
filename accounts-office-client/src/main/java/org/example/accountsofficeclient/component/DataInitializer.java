package org.example.accountsofficeclient.component;

import org.example.accountsofficeclient.dto.client.ClientCreatedEventDTO;
import org.example.accountsofficeclient.entity.Address;
import org.example.accountsofficeclient.entity.City;
import org.example.accountsofficeclient.entity.Client;
import org.example.accountsofficeclient.event.ClientEventSender;
import org.example.accountsofficeclient.repository.AddressRepository;
import org.example.accountsofficeclient.repository.CityRepository;
import org.example.accountsofficeclient.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private final ClientService clientService;
    private final CityRepository cityRepository;
    private final AddressRepository addressRepository;
    private final ClientEventSender clientEventSender;
    private int numberOfClients;
    private List<String> clientNames = List.of(
            "Apple Inc.", "Microsoft Corporation", "Amazon.com, Inc.", "Alphabet Inc.",
            "Meta Platforms, Inc.", "Tesla, Inc.", "NVIDIA Corporation",
            "Oracle Corporation", "Adobe Inc.", "Netflix, Inc."
    );

    @Autowired
    public DataInitializer(ClientService clientService,
                           CityRepository cityRepository, AddressRepository addressRepository,
                           ClientEventSender clientEventSender) {
        this.clientService = clientService;
        this.cityRepository = cityRepository;
        this.addressRepository = addressRepository;
        this.clientEventSender = clientEventSender;
        this.numberOfClients = 5;
    }
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (clientService.findAll().isEmpty()) {
            List<City> cities = createAndSaveCities();

            List<Address> addresses = createAddresses(cities);
            addresses.forEach(addressRepository::save);

            List<Client> clients = createClients(addresses);
            clients.forEach(clientService::save);
            clients.forEach(client -> {
                ClientCreatedEventDTO dto = new ClientCreatedEventDTO();
                dto.setId(client.getId());
                dto.setName(client.getName());
                clientEventSender.sendClientCreatedEvent(dto);
            });
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
}
