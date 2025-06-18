package com.bankapi.bank_api.modules.transaction.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapi.bank_api.modules.transaction.entities.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID>  {}
