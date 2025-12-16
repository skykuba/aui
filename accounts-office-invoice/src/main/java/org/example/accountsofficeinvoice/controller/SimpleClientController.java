package org.example.accountsofficeinvoice.controller;

import org.example.accountsofficeinvoice.entity.SimpleClient;
import org.example.accountsofficeinvoice.service.SimpleClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
public class SimpleClientController {

    private final SimpleClientService simpleClientService;

    public SimpleClientController(SimpleClientService simpleClientService) {
        this.simpleClientService = simpleClientService;
    }

    /**
     * Utwórz nowego klienta
     * POST /api/clients
     */
    @PostMapping
    public ResponseEntity<SimpleClient> createClient(@RequestBody SimpleClient client) {
        SimpleClient createdClient = simpleClientService.createClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }

    /**
     * Pobierz klienta po UUID
     * GET /api/clients/{uuid}
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<SimpleClient> getClientById(@PathVariable UUID uuid) {
        Optional<SimpleClient> client = simpleClientService.getClientById(uuid);
        return client.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Pobierz wszystkich klientów
     * GET /api/clients
     */
    @GetMapping
    public ResponseEntity<List<SimpleClient>> getAllClients() {
        List<SimpleClient> clients = simpleClientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    /**
     * Aktualizuj klienta
     * PUT /api/clients/{uuid}
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<SimpleClient> updateClient(
            @PathVariable UUID uuid,
            @RequestBody SimpleClient clientDetails) {
        try {
            SimpleClient updatedClient = simpleClientService.updateClient(uuid, clientDetails);
            return ResponseEntity.ok(updatedClient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Usuń klienta wraz ze wszystkimi jego fakturami
     * DELETE /api/clients/{uuid}
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID uuid) {
        try {
            simpleClientService.deleteClient(uuid);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
}

