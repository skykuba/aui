package org.example.accountsofficeinvoice.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
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
    @GeneratedValue
    @UuidGenerator
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

    @JoinColumn(name = "client_uuid", nullable = false)
    private UUID clientUuid;

    @JoinColumn(name = "issuer_uuid", nullable = false)
    private UUID issuerUuid;
}
