package org.example.aui.core.service;

import org.example.aui.core.dto.client.ClientCreateUpdateDTO;
import org.example.aui.core.entity.Client;
import org.example.aui.core.mapper.ClientMapper;
import org.example.aui.core.repository.ClientRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {
    private final ClientRepository repository;
    private final AddressService addressService;
    private final InvoiceService invoiceService;


    public ClientService(ClientRepository repository, AddressService addressService, @Lazy InvoiceService invoiceService) {
        this.repository = repository;
        this.addressService = addressService;
        this.invoiceService = invoiceService;
    }

    @Transactional(readOnly = true)
    public Optional<Client> findById(UUID id) {
        return  repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Client> findAllWithAddressAndCity() {
        return repository.findAllWithAddressAndCity();
    }

    @Transactional
    public void save(Client client) {
        if (this.findClientByNip(client.getNip()).isEmpty()) {
            repository.save(client);
            return;
        }
        throw new IllegalArgumentException("Client with NIP " + client.getNip() + " already exists.");
    }

    @Transactional
    public Client saveFromDTO(ClientCreateUpdateDTO clientDTO) {
        if (clientDTO == null) {
            throw new IllegalArgumentException("Client data cannot be null");
        }

        Client client = ClientMapper.toEntity(clientDTO);

        client.setAddress(addressService.createFromDTO(clientDTO.getAddress()));

        save(client);

        return client;
    }

    @Transactional
    public Client updateFromDTO(UUID id, ClientCreateUpdateDTO client) {
        Client existingClient = this.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        if (!existingClient.getNip().equals(client.getNip())) {
            throw new IllegalArgumentException("Cannot change clients NIP");
        }

        existingClient.setName(client.getName());
        existingClient.setEmail(client.getEmail());

        existingClient.setAddress(addressService.createFromDTO(client.getAddress()));
        
        return existingClient;
    }

    @Transactional
    public void deleteByID(UUID id){
        Client client = getOrThrow(id);

        invoiceService.deleteAllByClient(client);

        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Client> findClientByNip(String nip){
        return repository.findClientByNip(nip);
    }
    @Transactional(readOnly = true)
    public List<Client> findAllClientsWithUnpaidInvoices(Client issuer) {
        return repository.findAllClientsWithUnpaidInvoices(issuer);
    }

    @Transactional(readOnly = true)
    public Client getOrThrow(UUID clientUuid) {
        return findById(clientUuid)
                .orElseThrow(() -> new IllegalArgumentException("Client with UUID " + clientUuid + " not found"));
    }
}
