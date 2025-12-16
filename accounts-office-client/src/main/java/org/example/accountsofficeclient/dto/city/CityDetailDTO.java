package org.example.accountsofficeclient.dto.city;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
