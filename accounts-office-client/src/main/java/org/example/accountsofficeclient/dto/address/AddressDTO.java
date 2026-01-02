package org.example.accountsofficeclient.dto.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.accountsofficeclient.dto.city.CityDTO;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private UUID id;
    private String street;
    private String buildingNumber;
    private CityDTO city;
}
