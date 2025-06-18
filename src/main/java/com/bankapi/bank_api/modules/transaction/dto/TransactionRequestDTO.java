package com.bankapi.bank_api.modules.transaction.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.bankapi.bank_api.modules.transaction.entities.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {
  UUID accountId;
  TransactionType type;
  BigDecimal amount;
 }
