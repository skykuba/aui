package org.example.aui.core.repository;
import org.example.aui.core.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
}
