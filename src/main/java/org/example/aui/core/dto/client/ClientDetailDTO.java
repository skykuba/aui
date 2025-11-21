package org.example.aui.core.dto.client;

import lombok.*;
import org.example.aui.core.dto.address.AddressDetailDTO;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDetailDTO {

    private UUID id;
    private String name;
    private String email;
    private String nip;
    private AddressDetailDTO address;
}
