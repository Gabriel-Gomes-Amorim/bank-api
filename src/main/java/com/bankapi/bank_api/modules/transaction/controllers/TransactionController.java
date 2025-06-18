package com.bankapi.bank_api.modules.transaction.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapi.bank_api.modules.transaction.dto.TransactionRequestDTO;
import com.bankapi.bank_api.modules.transaction.entities.TransactionEntity;
import com.bankapi.bank_api.modules.transaction.services.CreateTransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private CreateTransactionService transactionService;

    @PostMapping()
    @Operation(
        summary = "Create transactions",
        description = "Create transactions debit or credit between accounts"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Transactions created successfully",
            content = @Content(schema = @Schema(implementation = TransactionEntity.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Account not found"
        )
    })
    public ResponseEntity<Object> create(@RequestBody List<TransactionRequestDTO> transactions) {
        try {
            var result = this.transactionService.execute(transactions);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
               return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
