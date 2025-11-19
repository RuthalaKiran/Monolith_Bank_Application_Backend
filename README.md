#  Banking System 

## 🔹Overview

This project is a **Banking Services Application** — a **Spring Boot monolithic backend system** that simulates real-world banking operations.

The main goal of this project is to **transform a Core Java Banking System into a fully functional Spring Boot application** with **RESTful APIs**, allowing users to:

- Manage bank accounts
- Perform transactions
- Persist data using **MongoDB**
- Handle exceptions gracefully

## 🔹 Core Functionalities

The Banking System provides the following key operations:

- **Account Creation** – Create new bank accounts with necessary details.
- **Transactions** – Record and manage all types of transactions.
- **Deposit** – Add money to an existing account.
- **Withdraw** – Withdraw money from an account while validating balance.
- **Transfer** – Transfer funds between accounts securely.
- **View Transactions** – Retrieve and view transaction history for accounts.

> These functionalities are exposed via **RESTful APIs** and implemented using a **layered architecture** with full data persistence in **MongoDB**.

## 🔹 Project / Folder Structure

The project follows a **layered architecture** with a clear separation of concerns:

src/  
└── main/  
&emsp;├── java/  
&emsp;│&emsp;└── com.bankapp/  
&emsp;│&emsp;&emsp;├── model/  *(MongoDB Entities)*  
&emsp;│&emsp;&emsp;├── service/  *(Business logic layer)*  
&emsp;│&emsp;&emsp;├── repository/  *(Data access layer)*  
&emsp;│&emsp;&emsp;├── controller/  *(REST API endpoints)*  
&emsp;│&emsp;&emsp;├── enums/  *(Enum definitions)*  
&emsp;│&emsp;&emsp;├── util/  *(Utility / helper classes)*  
&emsp;│&emsp;&emsp;└── exceptions/  *(Custom exceptions & GlobalExceptionHandler)*  
&emsp;└── resources/  
&emsp;&emsp;└── application.properties

## 🔹 Exception Handling

The project implements **custom exception handling** to ensure robust and user-friendly error management.

### Custom Exceptions
- **InvalidAmountException** – Thrown when an invalid amount (e.g., negative or zero) is provided for deposit, withdrawal, or transfer.
- **InsufficientBalanceException** – Thrown when an account does not have enough balance to complete a withdrawal or transfer.
- **AccountNotFoundException** – Thrown when an account ID is not found in the system.

### Global Exception Handling
- Implemented using **`@ControllerAdvice`** and **`@ExceptionHandler`**.
- All exceptions are handled centrally, ensuring **standardized API error responses**.
- Example of handled errors:
    - Invalid deposit/withdraw amount
    - Insufficient funds for transaction
    - Non-existent account access

> This approach ensures that all API endpoints return **consistent and meaningful error messages** without duplicating try-catch logic in controllers or services.

## 🔹 API Endpoints

| Operation       | HTTP Method | Endpoint                                | Description                             |
|-----------------|------------|----------------------------------------|-----------------------------------------|
| Create Account  | POST       | `/api/accounts`                         | Create a new account                     |
| Get Account     | GET        | `/api/accounts/{accountNumber}`         | Retrieve account details                 |
| Deposit         | PUT        | `/api/accounts/{accountNumber}/deposit`| Deposit funds into an account            |
| Withdraw        | PUT        | `/api/accounts/{accountNumber}/withdraw`| Withdraw funds from an account           |
| Transfer        | POST       | `/api/accounts/transfer`                | Transfer funds between accounts          |
| Transactions    | GET        | `/api/accounts/{accountNumber}/transactions` | Fetch all transactions for an account |


## 🔹 Unit Test Coverage

The project has **top-tier unit testing** with **91% overall coverage**. Detailed coverage by package:

| Package            | Instructions Missed / Coverage | Branches Missed / Coverage | Lines Missed / Coverage | Methods Missed / Coverage | Notes |
|-------------------|-------------------------------|----------------------------|------------------------|--------------------------|-------|
| **Total**          | 99 of 1,166 → **91%**        | 9 of 36 → 75%             | 309 missed / 154       | 136 missed / 12          | 24 classes |
| **com.exceptions** | 32 / 227 → 45%               | n/a                        | 19 / 10                | 10 / 5                    | Custom exceptions coverage low (expected) |
| **com.dto**        | 30 / 223 → 88%               | n/a                        | 89 / 61                | 61 / 7                    | DTOs well covered |
| **com.service**    | 155 / 28 → 97%               | 826 / 76%                  | 111 / 2                | 16 / 0                    | Service layer highly covered |
| **com.main**       | 8 / 27%                       | n/a                        | 4 / 3                  | 2 / 0                     | Main class has minimal logic |
| **com.model**      | 710 / 9 → 93%                | n/a                        | 50 / 3                 | 32 / 0                    | Models mostly covered |
| **com.utils**      | 74 / 2 → 85%                 | 11 / 50%                    | 9 / 2                  | 4 / 0                     | Utility classes covered |
| **com.controller** | 78 / 100%                     | n/a                        | 16 / 0                 | 8 / 0                     | Controller fully tested |
| **com.enums**      | 57 / 100%                     | n/a                        | 11 / 0                 | 3 / 0                     | Enums fully covered |

> ✅ Coverage above **70%** is recommended in enterprise projects.  
> ✅ Service and Controller layers are highly tested, ensuring **core logic reliability**.


## 🔹 Conclusion

The **Banking System Simulator** is a **fully functional Spring Boot application** with:

- RESTful APIs for accounts and transactions
- MongoDB persistence
- Custom exception handling with `@ControllerAdvice`
- Clean layered architecture with DTOs, utils, and enums
- **91% unit test coverage**, ensuring reliability

This project is **industry-ready**, well-tested, and perfect for learning, portfolio, or interview showcase.
