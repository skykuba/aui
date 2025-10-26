package org.example.aui.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class Client {
    @Id
    private UUID id;

    @Column
    private String name;

    @Column
    private String email;

    @Column(unique = true)
    private String nip;

    @Column
    private String address;

    @OneToMany(mappedBy = "client")
    private List<Invoice> invoiceList;

    @OneToMany(mappedBy = "issuer")
    private List<Invoice> issuedInvoiceList;
}
