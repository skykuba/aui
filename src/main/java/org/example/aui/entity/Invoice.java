package org.example.aui.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="invoices")
public class Invoice {
    @Id
    private UUID uuid;

    @Column
    private String invoiceId;

    @Column(name = "net_amount")
    private Double netAmount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column
    private Boolean paid;

    @ManyToOne
    @JoinColumn(name = "client_uuid")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "issuer_uuid")
    private Client issuer;
}
