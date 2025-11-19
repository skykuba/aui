package org.example.aui.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="invoices", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"issuer_uuid", "invoiceId"})
})
public class Invoice {
    @Id
    private UUID uuid;

    @Column(nullable = false)
    private String invoiceId;

    @Column(name = "net_amount")
    private Double netAmount;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime dateTime;

    @Column
    private Boolean paid;

    @ManyToOne
    @JoinColumn(name = "client_uuid")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "issuer_uuid")
    private Client issuer;
}
