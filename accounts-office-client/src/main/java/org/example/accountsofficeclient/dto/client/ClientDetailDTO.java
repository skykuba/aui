package org.example.accountsofficeclient.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.accountsofficeclient.dto.address.AddressDetailDTO;

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
