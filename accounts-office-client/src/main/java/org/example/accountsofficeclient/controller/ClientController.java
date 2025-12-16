package org.example.accountsofficeclient.controller;

import org.example.accountsofficeclient.dto.client.ClientCreateUpdateDTO;
import org.example.accountsofficeclient.dto.client.ClientDTO;
import org.example.accountsofficeclient.dto.client.ClientDetailDTO;
import org.example.accountsofficeclient.mapper.ClientMapper;
import org.example.accountsofficeclient.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
    
    @GetMapping("/")
    public List<ClientDTO> getListAllClients() {
        return clientService.findAll().stream().map(ClientMapper::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{clientId}")
    public ClientDetailDTO getClientDetail(@PathVariable UUID clientId) {
        return clientService.findById(clientId).map(ClientMapper::toDetailDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
    }

    @PutMapping("/{clientId}")
    public void updateClient(@PathVariable UUID clientId, @RequestBody ClientCreateUpdateDTO dto) {
        clientService.updateFromDTO(clientId, dto);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDetailDTO createClient(@RequestBody ClientCreateUpdateDTO dto) {
        return ClientMapper.toDetailDTO(clientService.saveFromDTO(dto));
    }

    @DeleteMapping("/{clientId}")
    public void deleteClient(@PathVariable UUID clientId) {
        clientService.deleteByID(clientId);
    }
}
