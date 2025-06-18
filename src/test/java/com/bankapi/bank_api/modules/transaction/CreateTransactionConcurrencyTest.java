package com.bankapi.bank_api.modules.transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bankapi.bank_api.modules.account.entities.AccountEntity;
import com.bankapi.bank_api.modules.account.repositories.AccountRepository;
import com.bankapi.bank_api.modules.transaction.dto.TransactionRequestDTO;
import com.bankapi.bank_api.modules.transaction.entities.TransactionType;
import com.bankapi.bank_api.modules.transaction.services.CreateTransactionService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CreateTransactionConcurrencyTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CreateTransactionService createTransactionService;

    @Test
    public void testConcurrentDebitTransactions() throws InterruptedException {
        UUID accountId = UUID.randomUUID();
        AccountEntity account = new AccountEntity();
        account.setId(accountId);
        account.setBalance(new BigDecimal("1000"));
        accountRepository.save(account);

        int numberOfThreads = 15; 

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        List<Callable<Boolean>> tasks = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            tasks.add(() -> {
                TransactionRequestDTO dto = new TransactionRequestDTO();
                dto.setAccountId(accountId);
                dto.setType(TransactionType.DEBIT);
                dto.setAmount(new BigDecimal("100"));

                try {
                    createTransactionService.execute(List.of(dto));
                    return true;
                } catch (IllegalArgumentException e) {
                    return false;
                }
            });
        }

        List<Future<Boolean>> results = executor.invokeAll(tasks);
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        long successCount = results.stream().filter(f -> {
            try {
                return f.get();
            } catch (Exception e) {
                return false;
            }
        }).count();

        AccountEntity updatedAccount = accountRepository.findById(accountId).orElseThrow();

        BigDecimal expectedBalance = new BigDecimal("1000").subtract(new BigDecimal(successCount * 100));


        assertThat(updatedAccount.getBalance()).isEqualByComparingTo(expectedBalance);
        assertThat(updatedAccount.getBalance()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
        assertThat(Long.valueOf(successCount)).isLessThanOrEqualTo(10L); 
    }
}
