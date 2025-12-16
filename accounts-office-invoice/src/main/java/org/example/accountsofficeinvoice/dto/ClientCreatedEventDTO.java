package org.example.accountsofficeinvoice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ClientCreatedEventDTO {
    private UUID id;
    private String name;
}