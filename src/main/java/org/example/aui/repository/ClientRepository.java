package org.example.aui.repository;

import org.example.aui.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findClientByNip(String nip);

    @Query("select distinct c from Client c join c.invoiceList i where i.paid = false and i.issuer = :issuer")
    List<Client> findAllClientsWithUnpaidInvoices(@Param("issuer") Client issuer);

}
