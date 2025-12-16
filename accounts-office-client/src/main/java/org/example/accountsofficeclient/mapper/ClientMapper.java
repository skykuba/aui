package org.example.accountsofficeclient.mapper;

import org.example.accountsofficeclient.dto.client.ClientCreateUpdateDTO;
import org.example.accountsofficeclient.dto.client.ClientDTO;
import org.example.accountsofficeclient.dto.client.ClientDetailDTO;
import org.example.accountsofficeclient.entity.Client;

public class ClientMapper {
    public static ClientDTO toDTO(Client client) {
        if (client == null) {
            return null;
        }
        return ClientDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .nip(client.getNip())
                .address(AddressMapper.toDTO(client.getAddress()))
                .build();
    }

    public static ClientDetailDTO toDetailDTO(Client client) {
        if (client == null) {
            return null;
        }
        return ClientDetailDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .email(client.getEmail())
                .nip(client.getNip())
                .address(AddressMapper.toDetailDTO(client.getAddress()))
                .build();
    }

    public static Client toEntity(ClientCreateUpdateDTO dto) {
        if (dto == null) {
            return null;
        }
        Client client = new Client();
        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        client.setNip(dto.getNip());
        return client;
    }
}

