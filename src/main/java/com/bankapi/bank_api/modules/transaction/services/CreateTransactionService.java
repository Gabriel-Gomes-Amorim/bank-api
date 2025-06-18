package com.bankapi.bank_api.modules.transaction.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankapi.bank_api.exceptions.InsufficientBalanceException;
import com.bankapi.bank_api.modules.account.entities.AccountEntity;
import com.bankapi.bank_api.modules.account.repositories.AccountRepository;
import com.bankapi.bank_api.modules.transaction.dto.TransactionRequestDTO;
import com.bankapi.bank_api.modules.transaction.entities.TransactionEntity;
import com.bankapi.bank_api.modules.transaction.entities.TransactionType;
import com.bankapi.bank_api.modules.transaction.repositories.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class CreateTransactionService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @Transactional
    public List<TransactionEntity> execute(List<TransactionRequestDTO> transactions) {
        List<TransactionEntity> createdTransactions = new ArrayList<>();

        for (TransactionRequestDTO dto : transactions) {
            AccountEntity account = this.accountRepository.findByIdForUpdate(dto.getAccountId())
                .orElseGet(() -> {
                    AccountEntity newAccount = new AccountEntity();
                    newAccount.setId(dto.getAccountId()); 
                    newAccount.setBalance(BigDecimal.ZERO);
                    return accountRepository.save(newAccount);
                });

            BigDecimal newBalance = account.getBalance();

            if (dto.getType() == TransactionType.DEBIT) {
                newBalance = newBalance.subtract(dto.getAmount());
               
                if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                    throw new InsufficientBalanceException();
                }
            } else {
                newBalance = newBalance.add(dto.getAmount());
            } 

            account.setBalance(newBalance);
            accountRepository.save(account);

            TransactionEntity transaction = new TransactionEntity();
            BeanUtils.copyProperties(dto, transaction);
            transaction.setAccount(account); 
            transactionRepository.save(transaction);

            createdTransactions.add(transaction);
        }

        return createdTransactions;
    }
}
