package com.controller;

import com.dto.*;
import com.service.AccountService;
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

    // create new account
    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> createAccount(@RequestBody AccountRequestDTO dto){
        AccountResponseDTO accountResponseDTO = accountService.createAccount(dto);
        return ResponseEntity.ok().body(ApiResponse.success("Accounted created successfully",accountResponseDTO));
    }

    // deposit money
    @PutMapping(value = "{accountNumber}/deposit",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> deposit(@PathVariable("accountNumber") String accountNumber, @RequestBody DepositRequestDTO dto){
        dto.setAccountNumber(accountNumber);
        TransactionResponseDTO transactionResponseDTO = accountService.deposit(dto);
        return ResponseEntity.ok().body(ApiResponse.success("Deposit successfully",transactionResponseDTO));
    }

    // withdraw money
    @PutMapping(value = "{accountNumber}/withdraw",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> withdraw(@PathVariable("accountNumber") String accountNumber,@RequestBody WithdrawRequestDTO dto){
        dto.setAccountNumber(accountNumber);
        TransactionResponseDTO transactionResponseDTO = accountService.withdraw(dto);
        return ResponseEntity.ok().body(ApiResponse.success("Withdrawal successfully",transactionResponseDTO));
    }

    // transfer money
    @PostMapping(value = "transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> transfer(@RequestBody TransferRequestDTO dto){
        TransactionResponseDTO transactionResponseDTO = accountService.transfer(dto);
        return ResponseEntity.ok().body(ApiResponse.success("Amount Transferred Successfully",transactionResponseDTO));
    }

    // retrieve account details
    @GetMapping(value = "{accountNumber}")
    public ResponseEntity<ApiResponse<?>> getAccountDetails(@PathVariable("accountNumber") String accountNumber){
        AccountResponseDTO accountResponseDTO = accountService.getAccountDetails(accountNumber);
        return ResponseEntity.ok().body(ApiResponse.success("Successfully fetched account details",accountResponseDTO));
    }

    // get all transactions by account number
    @GetMapping(value = "{accountNumber}/transactions",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> getAllTransactionsByAccount(@PathVariable("accountNumber") String accountNumber){
        List<TransactionResponseDTO> listOfTransaction = accountService.getTransactionsByAccount(accountNumber);
        return ResponseEntity.ok().body(ApiResponse.success("Successfully fetched transactions",listOfTransaction));
    }

}
















