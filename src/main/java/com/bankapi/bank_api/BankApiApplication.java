package com.bankapi.bank_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "Bank Api",
		version = "1.0",
		description = "API responsible for bank account management"
	)
)
public class BankApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApiApplication.class, args);
	}
}
