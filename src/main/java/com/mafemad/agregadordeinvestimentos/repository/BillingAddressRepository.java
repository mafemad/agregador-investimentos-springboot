package com.mafemad.agregadordeinvestimentos.repository;

import com.mafemad.agregadordeinvestimentos.entity.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BillingAddressRepository extends JpaRepository<BillingAddress, UUID> {
}
