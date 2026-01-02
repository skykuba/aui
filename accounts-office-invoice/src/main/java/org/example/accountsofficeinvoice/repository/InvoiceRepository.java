package org.example.accountsofficeinvoice.repository;


import org.example.accountsofficeinvoice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    List<Invoice> findAllByClientUuidAndPaid(UUID clientUuid, Boolean paid);
    Boolean existsByIssuerUuidAndInvoiceId(UUID issuerUuid, String invoiceId);
    List<Invoice> findAllByClientUuid(UUID clientUuid);
    List<Invoice> findAllByIssuerUuid(UUID issuerUuid);
    
    @Query("SELECT i FROM Invoice i WHERE i.clientUuid = :clientId OR i.issuerUuid = :issuerId")
    List<Invoice> findAllByClientOrIssuer(@Param("clientId") UUID clientId, @Param("issuerId") UUID issuerId);

    List<Invoice> getInvoiceByClientUuidAndUuid(UUID clientUuid, UUID uuid);

    Invoice getInvoiceByUuidAndClientUuid(UUID uuid, UUID clientUuid);
}
