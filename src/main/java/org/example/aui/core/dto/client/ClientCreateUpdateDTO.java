package org.example.aui.core.dto.client;

import lombok.*;
import org.example.aui.core.dto.address.AddressCreateUpdateDTO;

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
