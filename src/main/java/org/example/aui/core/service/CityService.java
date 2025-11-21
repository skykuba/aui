package org.example.aui.core.service;

import org.example.aui.core.dto.city.CityCreateUpdateDTO;
import org.example.aui.core.entity.City;
import org.example.aui.core.repository.CityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CityService {
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Transactional(readOnly = true)
    public Optional<City> findByDetails(String city, String state, String country) {
        return cityRepository.findByCityAndStateAndCountry(city, state, country);
    }

    @Transactional
    public City getOrCreate(CityCreateUpdateDTO cityDTO) {
        if (cityDTO == null) {
            return null;
        }

        return findByDetails(cityDTO.getCity(), cityDTO.getState(), cityDTO.getCountry())
                .orElseGet(() -> {
                    City newCity = new City();
                    newCity.setCity(cityDTO.getCity());
                    newCity.setState(cityDTO.getState());
                    newCity.setCountry(cityDTO.getCountry());
                    return cityRepository.save(newCity);
                });
    }
}

