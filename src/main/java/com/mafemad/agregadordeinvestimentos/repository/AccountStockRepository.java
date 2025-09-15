package com.mafemad.agregadordeinvestimentos.repository;

import com.mafemad.agregadordeinvestimentos.entity.AccountStock;
import com.mafemad.agregadordeinvestimentos.entity.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}
