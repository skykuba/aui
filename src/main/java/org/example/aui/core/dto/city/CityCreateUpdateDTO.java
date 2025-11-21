package org.example.aui.core.dto.city;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityCreateUpdateDTO {
    private String city;
    private String state;
    private String country;

}
