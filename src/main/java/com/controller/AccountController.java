package com.controller;

import com.dto.*;
import com.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/accounts")
public class AccountController {
    @Autowired
    AccountService accountService;

    Logger logger = LoggerFactory.getLogger(AccountController.class);

    // create new account
    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> createAccount(@RequestBody AccountRequestDTO dto){
        logger.info("Request to create account {}",dto.getHolderName());
        AccountResponseDTO accountResponseDTO = accountService.createAccount(dto);
        logger.info("Account created successfully :"+accountResponseDTO.getAccountNumber());
        return ResponseEntity.ok().body(ApiResponse.success("Accounted created successfully",accountResponseDTO));
    }

    // deposit money
    @PutMapping(value = "{accountNumber}/deposit",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> deposit(@PathVariable("accountNumber") String accountNumber, @RequestBody DepositRequestDTO dto){
        dto.setAccountNumber(accountNumber);
        logger.info("Deposit request received for account {}: amount {}", accountNumber, dto.getAmount());
        TransactionResponseDTO transactionResponseDTO = accountService.deposit(dto);
        logger.info("Deposit successful for account {}: transactionId {}", accountNumber, transactionResponseDTO.getTransactionId());
        return ResponseEntity.ok().body(ApiResponse.success("Deposit successfully",transactionResponseDTO));
    }

    // withdraw money
    @PutMapping(value = "{accountNumber}/withdraw",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> withdraw(@PathVariable("accountNumber") String accountNumber,@RequestBody WithdrawRequestDTO dto){
        dto.setAccountNumber(accountNumber);
        logger.info("Withdrawal request received for account {}: amount {}", accountNumber, dto.getAmount());
        TransactionResponseDTO transactionResponseDTO = accountService.withdraw(dto);
        logger.info("Withdrawal successful for account {}: transactionId {}", accountNumber, transactionResponseDTO.getTransactionId());
        return ResponseEntity.ok().body(ApiResponse.success("Withdrawal successfully",transactionResponseDTO));
    }

    // transfer money
    @PostMapping(value = "transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> transfer(@RequestBody TransferRequestDTO dto){
        logger.info("Transfer request from {} to {}: amount {}", dto.getFromAccount(), dto.getToAccount(), dto.getAmount());
        TransactionResponseDTO transactionResponseDTO = accountService.transfer(dto);
        logger.info("Transfer successful: transactionId {}", transactionResponseDTO.getTransactionId());
        return ResponseEntity.ok().body(ApiResponse.success("Amount Transferred Successfully",transactionResponseDTO));
    }

    // retrieve account details
    @GetMapping(value = "{accountNumber}")
    public ResponseEntity<ApiResponse<?>> getAccountDetails(@PathVariable("accountNumber") String accountNumber){
        logger.info("Request to fetch account details: {}", accountNumber);
        AccountResponseDTO accountResponseDTO = accountService.getAccountDetails(accountNumber);
        logger.info("Fetched account details successfully: {}", accountNumber);
        return ResponseEntity.ok().body(ApiResponse.success("Successfully fetched account details",accountResponseDTO));
    }

    // get all transactions by account number
    @GetMapping(value = "{accountNumber}/transactions",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> getAllTransactionsByAccount(@PathVariable("accountNumber") String accountNumber){
        logger.info("Request to fetch all transactions for account: {}", accountNumber);
        List<TransactionResponseDTO> listOfTransaction = accountService.getTransactionsByAccount(accountNumber);
        logger.info("Fetched {} transactions for account {}", listOfTransaction.size(), accountNumber);
        return ResponseEntity.ok().body(ApiResponse.success("Successfully fetched transactions",listOfTransaction));
    }

}
















