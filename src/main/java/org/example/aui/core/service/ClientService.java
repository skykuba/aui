package org.example.aui.core.service;

import org.example.aui.core.entity.Client;
import org.example.aui.core.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {
    private final ClientRepository repository;


    public ClientService(ClientRepository repository) {
        this.repository = repository;
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
    public Client save(Client client){
        return repository.save(client);
    }

    @Transactional
    public void deleteByID(UUID id){
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
}
