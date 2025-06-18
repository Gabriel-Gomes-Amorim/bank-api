# Bank API

A RESTful API for managing bank account transactions including debit and credit operations.

## Description

This project implements an API that handles financial transactions on bank accounts, allowing debit and credit entries, including multiple transactions in a single request. The API also provides functionality to retrieve the current balance of a specific account.

The system is designed to be thread-safe, ensuring data consistency and integrity even under concurrent requests.

## Technologies Used

- Java 21
- Spring Boot
- Spring Data JPA (Hibernate)
- H2 Database (for development and testing)
- Swagger / OpenAPI for API documentation
- JUnit and Mockito for automated testing

## Features

- Perform debit and credit transactions on accounts
- Support multiple transactions in a single request
- Retrieve current balance of a specific account
- Thread-safe operations to avoid race conditions and maintain data consistency

## Main Endpoints

### 1. Perform Transactions

- **POST** `/transactions`
- **Request Body**: List of transactions including account ID, type (debit/credit), and amount
- **Response**: List of created transactions with details

### 2. Get Account Balance

- **GET** `/accounts/{accountId}/balance`
- **Response**: Current balance of the specified account
