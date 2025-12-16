package org.example.accountsofficeclient.dto.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.accountsofficeclient.dto.city.CityCreateUpdateDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressCreateUpdateDTO {

    private String street;
    private String buildingNumber;
    private CityCreateUpdateDTO city;

}
