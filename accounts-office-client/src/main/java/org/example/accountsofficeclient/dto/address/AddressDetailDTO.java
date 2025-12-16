package org.example.accountsofficeclient.dto.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.accountsofficeclient.dto.city.CityDetailDTO;

import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDetailDTO {

    private UUID id;
    private String street;
    private String buildingNumber;
    private CityDetailDTO city;
}
