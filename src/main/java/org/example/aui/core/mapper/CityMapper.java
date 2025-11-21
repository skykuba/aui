package org.example.aui.core.mapper;

import org.example.aui.core.dto.city.CityCreateUpdateDTO;
import org.example.aui.core.dto.city.CityDTO;
import org.example.aui.core.dto.city.CityDetailDTO;
import org.example.aui.core.entity.City;

public class CityMapper {
    public static CityDTO toDTO(City city) {
        if (city == null) {
            return null;
        }
        return CityDTO.builder()
                .id(city.getId())
                .city(city.getCity())
                .country(city.getCountry())
                .build();
    }

    public static CityDetailDTO toDetailDTO(City city) {
        if (city == null) {
            return null;
        }
        return CityDetailDTO.builder()
                .id(city.getId())
                .city(city.getCity())
                .state(city.getState())
                .country(city.getCountry())
                .build();
    }

    public static City toEntity(CityCreateUpdateDTO dto) {
        if (dto == null) {
            return null;
        }
        City city = new City();
        city.setCity(dto.getCity());
        city.setState(dto.getState());
        city.setCountry(dto.getCountry());
        return city;
    }
}

