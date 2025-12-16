package org.example.accountsofficeclient.event;

import org.example.accountsofficeclient.dto.client.ClientCreatedEventDTO;
import org.example.accountsofficeclient.dto.client.ClientUpdateEventDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class ClientEventSender {
    private final String baseUrl;
    private final RestTemplate restTemplate;

    public ClientEventSender(@Value("${invoice.service.url}") String invoiceServiceUrl, RestTemplate restTemplate) {
        this.baseUrl = invoiceServiceUrl + "/api/event";
        this.restTemplate = restTemplate;
    }

    public void sendClientCreatedEvent(ClientCreatedEventDTO event) {
        try {
            restTemplate.postForObject(baseUrl + "/client-created", event, Void.class);
        } catch (Exception e) {
            // Log error but don't fail the operation
            System.err.println("Failed to send client created event: " + e.getMessage());
        }
    }

    public void sendClientUpdatedEvent(UUID clientId, ClientUpdateEventDTO event) {
        try {
            restTemplate.postForObject(baseUrl + "/client-updated/" + clientId, event, Void.class);
        } catch (Exception e) {
            // Log error but don't fail the operation
            System.err.println("Failed to send client updated event: " + e.getMessage());
        }
    }

    public void sendClientDeletedEvent(String clientId) {
        try {
            restTemplate.delete(baseUrl + "/client/" + clientId);
        } catch (Exception e) {
            // Log error but don't fail the operation
            System.err.println("Failed to send client deleted event: " + e.getMessage());
        }
    }
}
