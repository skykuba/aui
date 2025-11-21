package org.example.aui.core.service;

import org.example.aui.core.dto.address.AddressCreateUpdateDTO;
import org.example.aui.core.entity.Address;
import org.example.aui.core.entity.City;
import org.example.aui.core.mapper.AddressMapper;
import org.example.aui.core.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final CityService cityService;

    public AddressService(AddressRepository addressRepository, CityService cityService) {
        this.addressRepository = addressRepository;
        this.cityService = cityService;
    }

    @Transactional(readOnly = true)
    public Optional<Address> findById(UUID id) {
        return addressRepository.findById(id);
    }

    @Transactional
    public Address createFromDTO(AddressCreateUpdateDTO addressDTO) {
        if (addressDTO == null) {
            return null;
        }

        City city = cityService.getOrCreate(addressDTO.getCity());

        Address address = AddressMapper.toEntity(addressDTO, city);
        return addressRepository.save(address);
    }

    @Transactional
    public void save(Address address) {
        addressRepository.save(address);
    }
}

