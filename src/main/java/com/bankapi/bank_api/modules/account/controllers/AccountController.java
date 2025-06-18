package com.bankapi.bank_api.modules.account.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapi.bank_api.modules.account.services.GetAccountBalanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private GetAccountBalanceService getAccountBalanceService;

    @GetMapping("/{id}/balance")
    @Operation(summary = "Get account balance", description = "Returns the current account balance by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Balance found successfully", content = 
            @Content(schema = @Schema(implementation = java.math.BigDecimal.class))),
        @ApiResponse(responseCode = "400", description = "Account not found")
    })
    public ResponseEntity<Object> getBalance(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok().body(getAccountBalanceService.execute(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
