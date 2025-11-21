package org.example.aui.core.dto.city;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityDetailDTO {
    private UUID id;
    private String city;
    private String state;
    private String country;
}
