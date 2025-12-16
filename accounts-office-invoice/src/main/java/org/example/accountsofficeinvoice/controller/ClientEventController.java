package org.example.accountsofficeinvoice.controller;

import org.example.accountsofficeinvoice.dto.ClientCreatedEventDTO;
import org.example.accountsofficeinvoice.entity.SimpleClient;
import org.example.accountsofficeinvoice.service.SimpleClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
public class ClientEventController {

    private final SimpleClientService simpleClientService;

    public ClientEventController(SimpleClientService simpleClientService){
        this.simpleClientService = simpleClientService;
    }

    @PostMapping("/client-created")
    public ResponseEntity<Void> createSimpleClient(@RequestBody ClientCreatedEventDTO dto){
        simpleClientService.createClient(convertToEntity(dto));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/client-deleted/{clientUuid}")
    public ResponseEntity<Void> deleteClient(@PathVariable String clientUuid){
        simpleClientService.deleteClient(java.util.UUID.fromString(clientUuid));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/client-updated/{clientUuid}")
    public ResponseEntity<SimpleClient> updateClient(@PathVariable String clientUuid, @RequestBody ClientCreatedEventDTO dto){
        SimpleClient updatedClient = simpleClientService.updateClient(java.util.UUID.fromString(clientUuid), convertToEntity(dto));
        return ResponseEntity.ok(updatedClient);
    }

    private SimpleClient convertToEntity(ClientCreatedEventDTO dto){
        SimpleClient client = new SimpleClient();
        client.setId(dto.getId());
        client.setName(dto.getName());
        return client;
    }
}
