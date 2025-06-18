package com.bankapi.bank_api.modules.account;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import com.bankapi.bank_api.exceptions.AccountNotFoundException;
import com.bankapi.bank_api.modules.account.entities.AccountEntity;
import com.bankapi.bank_api.modules.account.repositories.AccountRepository;
import com.bankapi.bank_api.modules.account.services.GetAccountBalanceService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GetAccountBalanceServiceTest {

    @InjectMocks
    private GetAccountBalanceService getAccountBalanceService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    @DisplayName("should return account balance when account exists")
    void shouldReturnAccountBalance() {
        UUID accountId = UUID.randomUUID();
        BigDecimal balance = new BigDecimal("1500.50");

        AccountEntity account = new AccountEntity();
        account.setId(accountId);
        account.setBalance(balance);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        BigDecimal result = getAccountBalanceService.execute(accountId);

        assertThat(result).isEqualByComparingTo(balance);
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    @DisplayName("should throw AccountNotFoundException when account does not exist")
    void shouldThrowWhenAccountNotFound() {
        UUID accountId = UUID.randomUUID();

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getAccountBalanceService.execute(accountId))
            .isInstanceOf(AccountNotFoundException.class);

        verify(accountRepository, times(1)).findById(accountId);
    }
}
