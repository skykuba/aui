package org.example.aui.core.dto.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.aui.core.dto.city.CityDTO;
import org.example.aui.core.dto.city.CityDetailDTO;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private UUID id;
    private String street;
    private CityDTO city;
}
