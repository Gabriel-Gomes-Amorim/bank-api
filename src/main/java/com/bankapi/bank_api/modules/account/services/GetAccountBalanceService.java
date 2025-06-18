package com.bankapi.bank_api.modules.account.services;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankapi.bank_api.exceptions.AccountNotFoundException;
import com.bankapi.bank_api.modules.account.entities.AccountEntity;
import com.bankapi.bank_api.modules.account.repositories.AccountRepository;

@Service
public class GetAccountBalanceService {

    @Autowired
    private AccountRepository accountRepository;

    public BigDecimal execute(UUID accountId) {
        AccountEntity account = this.accountRepository.findById(accountId)
                    .orElseThrow(() -> {
                        throw new AccountNotFoundException();
                    });

        return account.getBalance();
    }
}
