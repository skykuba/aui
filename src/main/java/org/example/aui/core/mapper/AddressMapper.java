package org.example.aui.core.mapper;

import org.example.aui.core.dto.address.AddressCreateUpdateDTO;
import org.example.aui.core.dto.address.AddressDTO;
import org.example.aui.core.dto.address.AddressDetailDTO;
import org.example.aui.core.entity.Address;
import org.example.aui.core.entity.City;

public class AddressMapper {
    public static AddressDTO toDTO(Address address) {
        if (address == null) {
            return null;
        }
        return AddressDTO.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(CityMapper.toDTO(address.getCity()))
                .build();
    }

    public static AddressDetailDTO toDetailDTO(Address address) {
        if (address == null) {
            return null;
        }
        return AddressDetailDTO.builder()
                .id(address.getId())
                .street(address.getStreet())
                .buildingNumber(address.getBuildingNumber())
                .city(CityMapper.toDetailDTO(address.getCity()))
                .build();
    }

    public static Address toEntity(AddressCreateUpdateDTO dto, City city) {
        if (dto == null) {
            return null;
        }
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setBuildingNumber(dto.getBuildingNumber());
        address.setCity(city);
        return address;
    }
}

