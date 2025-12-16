package org.example.accountsofficeclient.mapper;

import org.example.accountsofficeclient.dto.address.AddressCreateUpdateDTO;
import org.example.accountsofficeclient.dto.address.AddressDTO;
import org.example.accountsofficeclient.dto.address.AddressDetailDTO;
import org.example.accountsofficeclient.entity.Address;
import org.example.accountsofficeclient.entity.City;

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

