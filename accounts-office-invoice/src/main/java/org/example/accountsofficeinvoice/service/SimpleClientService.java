package org.example.accountsofficeinvoice.service;

import org.example.accountsofficeinvoice.component.ClientCreatedEvent;
import org.example.accountsofficeinvoice.entity.SimpleClient;
import org.example.accountsofficeinvoice.repository.SimpleClientRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SimpleClientService {

    private final SimpleClientRepository simpleClientRepository;
    private final ApplicationEventPublisher eventPublisher;

    public SimpleClientService(SimpleClientRepository simpleClientRepository, ApplicationEventPublisher eventPublisher) {
        this.simpleClientRepository = simpleClientRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Utwórz nowego klienta
     */
    public SimpleClient createClient(SimpleClient client) {
        if (client.getId() == null) {
            client.setId(UUID.randomUUID());
        }
        SimpleClient savedClient = simpleClientRepository.save(client);
        eventPublisher.publishEvent(new ClientCreatedEvent(savedClient));
        return savedClient;
    }

    /**
     * Pobierz klienta po UUID
     */
    public Optional<SimpleClient> getClientById(UUID uuid) {
        return simpleClientRepository.findById(uuid);
    }

    /**
     * Pobierz wszystkich klientów
     */
    public List<SimpleClient> getAllClients() {
        return simpleClientRepository.findAll();
    }

    /**
     * Aktualizuj klienta
     */
    public SimpleClient updateClient(UUID uuid, SimpleClient clientDetails) {
        Optional<SimpleClient> clientOptional = simpleClientRepository.findById(uuid);

        if (clientOptional.isPresent()) {
            SimpleClient client = clientOptional.get();

            if (clientDetails.getName() != null && !clientDetails.getName().isEmpty()) {
                client.setName(clientDetails.getName());
            }

            return simpleClientRepository.save(client);
        }

        throw new IllegalArgumentException("Klient o UUID " + uuid + " nie znaleziony");
    }

    /**
     * Usuń klienta
     */
    public void deleteClient(UUID uuid) {
        if (!simpleClientRepository.existsById(uuid)) {
            throw new IllegalArgumentException("Klient o UUID " + uuid + " nie znaleziony");
        }
        simpleClientRepository.deleteById(uuid);
    }

    /**
     * Sprawdź, czy klient istnieje
     */
    public Boolean clientExists(UUID uuid) {
        return simpleClientRepository.existsById(uuid);
    }
}
