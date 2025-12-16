package org.example.accountsofficeclient.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.accountsofficeclient.dto.address.AddressCreateUpdateDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientCreateUpdateDTO {

    private String name;
    private String email;
    private String nip;
    private AddressCreateUpdateDTO address;
}
