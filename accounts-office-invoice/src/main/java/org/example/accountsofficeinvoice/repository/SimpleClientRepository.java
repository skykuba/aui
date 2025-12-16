package org.example.accountsofficeinvoice.repository;

import org.example.accountsofficeinvoice.entity.Invoice;
import org.example.accountsofficeinvoice.entity.SimpleClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface SimpleClientRepository  extends JpaRepository<SimpleClient, UUID> {

}
