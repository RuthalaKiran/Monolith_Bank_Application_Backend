package com.service;

import com.dto.*;
import com.enums.AccountStatus;
import com.enums.TransactionStatus;
import com.enums.TransactionType;
import com.exceptions.AccountNotFoundException;
import com.exceptions.InsufficientBalanceException;
import com.exceptions.InvalidAmountException;
import com.exceptions.InvalidInputException;
import com.model.Account;
import com.model.Transactions;
import com.repository.AccountRepository;
import com.repository.TransactionRepository;
import com.utils.AccountNumberGenerator;
import com.utils.TransactionIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    // create account
    public AccountResponseDTO createAccount(AccountRequestDTO dto){
        if(dto.getHolderName() == null || dto.getHolderName().trim().isEmpty()){
            throw new InvalidInputException(" Account holder name is required");
        }

        // calling generate account number in loop so that no two user get same account number
        String accountNumber = AccountNumberGenerator.generateAccountNum(dto.getHolderName());
        while (accountRepository.existsByAccountNumber(accountNumber)){
            accountNumber = AccountNumberGenerator.generateAccountNum(dto.getHolderName());
        }

        // add DTO info to actual account class
        Account acc = new Account();
        acc.setHolderName(dto.getHolderName());
        acc.setAccountNumber(accountNumber);
        acc.setBalance(0.0);
        acc.setStatus(AccountStatus.ACTIVE);
        acc.setCreatedAt(LocalDateTime.now());
        // save account to db
        accountRepository.save(acc);

        return mapToAccountResponse(acc);
    }

    // deposit
    public TransactionResponseDTO deposit(DepositRequestDTO dto){
        if(dto.getAccountNumber() == null || dto.getAccountNumber().trim().isEmpty()){
            throw new InvalidInputException("Account number is required");
        }
        if(dto.getAmount() == null || dto.getAmount()<=0){
            throw new InvalidAmountException("Amount must be greater than 0");
        }
        Account accFromDb = accountRepository.findByAccountNumber(dto.getAccountNumber())
                        .orElseThrow(()->new AccountNotFoundException("Account not found"));

        // update balance
        accFromDb.setBalance(accFromDb.getBalance() + dto.getAmount());
        accountRepository.save(accFromDb);

        // fill transaction details to db
        Transactions t = new Transactions();
        t.setTransactionId(TransactionIdGenerator.generateTransactionId());
        t.setType(TransactionType.DEPOSIT);
        t.setAmount(dto.getAmount());
        t.setTimestamp(LocalDateTime.now());
        t.setStatus(TransactionStatus.SUCCESS);
        t.setSourceAccount(null);
        t.setDestinationAccount(accFromDb.getAccountNumber());

        // save transaction
        transactionRepository.save(t);

        // add transaction to account
        accFromDb.getTransactions().add(t);
        accountRepository.save(accFromDb);

        return mapToTxnResponse(t);
    }

    // withdraw
    public TransactionResponseDTO withdraw(WithdrawRequestDTO dto){
        if(dto.getAccountNumber() == null || dto.getAccountNumber().trim().isEmpty()){
            throw new InvalidInputException("Account number is required");
        }
        if(dto.getAmount() == null || dto.getAmount()<=0){
            throw new InvalidAmountException("Amount must be greater than 0");
        }
        Account accFromDb = accountRepository.findByAccountNumber(dto.getAccountNumber())
                .orElseThrow(()->new AccountNotFoundException("Account not found"));

        // validate withdraw balance
        if(accFromDb.getBalance() < dto.getAmount()){
            throw new InsufficientBalanceException("Insufficient balance");
        }
        // update balance
        accFromDb.setBalance(accFromDb.getBalance() - dto.getAmount());
        accountRepository.save(accFromDb);

        // fill transaction details
        Transactions t = new Transactions();
        t.setTransactionId(TransactionIdGenerator.generateTransactionId());
        t.setType(TransactionType.WITHDRAW);
        t.setAmount(dto.getAmount());
        t.setTimestamp(LocalDateTime.now());
        t.setStatus(TransactionStatus.SUCCESS);
        t.setSourceAccount(accFromDb.getAccountNumber());
        t.setDestinationAccount(null);

        transactionRepository.save(t);

        // add transaction to account
        accFromDb.getTransactions().add(t);
        accountRepository.save(accFromDb);

        return mapToTxnResponse(t);
    }

    // transfer
    public TransactionResponseDTO transfer(TransferRequestDTO dto){
        if(dto.getAmount() == null || dto.getAmount()<=0){
            throw new InvalidAmountException("Amount must be greater than 0");
        }
        // check if source and destination same
        if(dto.getFromAccount().equals(dto.getToAccount())){
            throw new InvalidInputException("Source and destination cannot be same");
        }

        // get accounts and validate
        Account sourceAcc = accountRepository.findByAccountNumber(dto.getFromAccount())
                .orElseThrow(()->new AccountNotFoundException("Account not found"));
        Account destinationAcc = accountRepository.findByAccountNumber(dto.getToAccount())
                .orElseThrow(()->new AccountNotFoundException("Account not found"));

        // validate source has amount
        if(sourceAcc.getBalance() < dto.getAmount()){
            throw new InsufficientBalanceException("Insufficient balance");
        }

        // update source and destination
        sourceAcc.setBalance(sourceAcc.getBalance() - dto.getAmount());
        destinationAcc.setBalance(destinationAcc.getBalance() + dto.getAmount());

        // fill transaction details
        Transactions t = new Transactions();
        t.setTransactionId(TransactionIdGenerator.generateTransactionId());
        t.setType(TransactionType.TRANSFER);
        t.setAmount(dto.getAmount());
        t.setTimestamp(LocalDateTime.now());
        t.setStatus(TransactionStatus.SUCCESS);
        t.setSourceAccount(sourceAcc.getAccountNumber());
        t.setDestinationAccount(destinationAcc.getAccountNumber());

        // save transaction
        transactionRepository.save(t);

        // add transaction to both accounts
        sourceAcc.getTransactions().add(t);
        destinationAcc.getTransactions().add(t);

        // save both
        accountRepository.save(sourceAcc);
        accountRepository.save(destinationAcc);

        return mapToTxnResponse(t);
    }

    // retrieve account details
    public AccountResponseDTO getAccountDetails(String accountNumber){
        Account acc = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()->new AccountNotFoundException("Account not found"));
        return mapToAccountResponse(acc);
    }

    // get all transactions by account number
    public List<TransactionResponseDTO> getTransactionsByAccount(String accountNumber){
        List<Transactions> transactionsList = accountRepository.findByAccountNumber(accountNumber)
                                 .orElseThrow(()-> new AccountNotFoundException("Account not found")).getTransactions();
        List<TransactionResponseDTO> list = new ArrayList<>();
        // convert to TransactionResponse DTO
        for(Transactions t: transactionsList){
            TransactionResponseDTO dto = mapToTxnResponse(t);
            list.add(dto);
        }
        return list;
    }




    // map to Account Response DTO
    private AccountResponseDTO mapToAccountResponse(Account acc){
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setId(acc.getId());
        dto.setAccountNumber(acc.getAccountNumber());
        dto.setHolderName(acc.getHolderName());
        dto.setBalance(acc.getBalance());
        dto.setStatus(acc.getStatus());
        dto.setCreatedAt(acc.getCreatedAt());
        return dto;
    }

    // map to Transaction Response
    private TransactionResponseDTO mapToTxnResponse(Transactions t){
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setTransactionId(t.getTransactionId());
        dto.setType(t.getType());
        dto.setAmount(t.getAmount());
        dto.setStatus(t.getStatus());
        dto.setSourceAccount(t.getSourceAccount());
        dto.setDestinationAccount(t.getDestinationAccount());
        dto.setTimestamp(t.getTimestamp());
        return dto;
    }
}
