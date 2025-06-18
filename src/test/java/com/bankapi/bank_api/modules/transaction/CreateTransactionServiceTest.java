package com.bankapi.bank_api.modules.transaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.bankapi.bank_api.exceptions.InsufficientBalanceException;
import com.bankapi.bank_api.modules.account.entities.AccountEntity;
import com.bankapi.bank_api.modules.account.repositories.AccountRepository;
import com.bankapi.bank_api.modules.transaction.dto.TransactionRequestDTO;
import com.bankapi.bank_api.modules.transaction.entities.TransactionEntity;
import com.bankapi.bank_api.modules.transaction.entities.TransactionType;
import com.bankapi.bank_api.modules.transaction.repositories.TransactionRepository;
import com.bankapi.bank_api.modules.transaction.services.CreateTransactionService;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateTransactionServiceTest {

    @InjectMocks
    private CreateTransactionService createTransactionService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;


    @Test
    @DisplayName("should create credit transaction successfully")
    void shouldCreateCreditTransactionSuccessfully() {
        UUID accountId = UUID.randomUUID();
        BigDecimal initialBalance = new BigDecimal("200.00");
        BigDecimal creditAmount = new BigDecimal("100.00");
        BigDecimal expectedBalance = initialBalance.add(creditAmount);

        AccountEntity account = new AccountEntity();
        account.setId(accountId);
        account.setBalance(initialBalance);

        var dto = new TransactionRequestDTO(accountId, TransactionType.CREDIT, creditAmount);

        when(accountRepository.findByIdForUpdate(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(AccountEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionRepository.save(any(TransactionEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = createTransactionService.execute(List.of(dto));

        assertThat(account.getBalance()).isEqualByComparingTo(expectedBalance);

        assertThat(result).hasSize(1);
        TransactionEntity createdTransaction = result.get(0);
        assertThat(createdTransaction.getAccount().getId()).isEqualTo(accountId);
        assertThat(createdTransaction.getAmount()).isEqualByComparingTo(creditAmount);
        assertThat(createdTransaction.getType()).isEqualTo(TransactionType.CREDIT);

        verify(accountRepository, times(1)).save(account);
        verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
    }

    @Test
    @DisplayName("should create debit transaction successfully")
    void shouldCreateDebitTransactionSuccessfully() {
        UUID accountId = UUID.randomUUID();
        BigDecimal initialBalance = new BigDecimal("200.00");
        BigDecimal debitAmount = new BigDecimal("50.00");
        BigDecimal expectedBalance = initialBalance.subtract(debitAmount);

        AccountEntity account = new AccountEntity();
        account.setId(accountId);
        account.setBalance(initialBalance);

        var dto = new TransactionRequestDTO(accountId, TransactionType.DEBIT, debitAmount);

        when(accountRepository.findByIdForUpdate(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(AccountEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionRepository.save(any(TransactionEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = createTransactionService.execute(List.of(dto));

        assertThat(account.getBalance()).isEqualByComparingTo(expectedBalance);

        assertThat(result).hasSize(1);
        TransactionEntity createdTransaction = result.get(0);
        assertThat(createdTransaction.getAccount().getId()).isEqualTo(accountId);
        assertThat(createdTransaction.getAmount()).isEqualByComparingTo(debitAmount);
        assertThat(createdTransaction.getType()).isEqualTo(TransactionType.DEBIT);

        verify(accountRepository, times(1)).save(account);
        verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
    }

    @Test
    @DisplayName("should throw exception when debit amount is greater than balance")
    void shouldThrowWhenDebitAmountExceedsBalance() {
        UUID accountId = UUID.randomUUID();
        BigDecimal initialBalance = new BigDecimal("100.00");
        BigDecimal debitAmount = new BigDecimal("150.00");

        AccountEntity account = new AccountEntity();
        account.setId(accountId);
        account.setBalance(initialBalance);

        var dto = new TransactionRequestDTO(accountId, TransactionType.DEBIT, debitAmount);

        when(accountRepository.findByIdForUpdate(accountId)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> createTransactionService.execute(List.of(dto)))
            .isInstanceOf(InsufficientBalanceException.class);

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }
}
