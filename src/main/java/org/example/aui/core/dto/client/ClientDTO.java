package org.example.aui.core.dto.client;

import lombok.*;
import org.example.aui.core.dto.address.AddressDTO;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    private UUID id;
    private String name;
    private String nip;
    private AddressDTO address;
}
