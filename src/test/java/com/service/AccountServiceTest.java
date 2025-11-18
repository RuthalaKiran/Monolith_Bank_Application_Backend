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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    AccountService accountService;

    // ========== CREATE ACCOUNT ==========
    @Test
    void testCreateAccountSuccess() {
        AccountRequestDTO dto = new AccountRequestDTO();
        dto.setHolderName("Kiran");

        Account acc = new Account();
        acc.setId("1");
        acc.setHolderName("Kiran");
        acc.setAccountNumber("KIRA6771"); // dynamic value, no need to check exact
        acc.setBalance(0.0);
        acc.setStatus(AccountStatus.ACTIVE);
        acc.setCreatedAt(LocalDateTime.now());

        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(acc);

        AccountResponseDTO response = accountService.createAccount(dto);

        Assertions.assertEquals("Kiran", response.getHolderName());

        // Don't check exact account number — it's random
        Assertions.assertNotNull(response.getAccountNumber());
        Assertions.assertFalse(response.getAccountNumber().isEmpty());
        System.out.println("done with account creation");
    }


    @Test
    void testCreateAccountMissingName() {
        AccountRequestDTO dto = new AccountRequestDTO();
        dto.setHolderName("");

        Assertions.assertThrows(InvalidInputException.class, () -> {
            accountService.createAccount(dto);
        });
        System.out.println("done with create account with missing name");
    }



    // ========== DEPOSIT ==========
    @Test
    void testDepositSuccess() {
        DepositRequestDTO dto = new DepositRequestDTO();
        dto.setAccountNumber("ACC001");
        dto.setAmount(100.0);

        Account acc = new Account();
        acc.setAccountNumber("ACC001");
        acc.setBalance(500.0);
        acc.setTransactions(new ArrayList<>());

        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(acc));
        when(transactionRepository.save(any(Transactions.class))).thenAnswer(i -> i.getArguments()[0]);
        when(accountRepository.save(any(Account.class))).thenReturn(acc);

        TransactionResponseDTO response = accountService.deposit(dto);

        Assertions.assertEquals(600.0, acc.getBalance());
        Assertions.assertEquals(TransactionType.DEPOSIT, response.getType());
        Assertions.assertEquals(TransactionStatus.SUCCESS, response.getStatus());
    }

    @Test
    void testdepsuccess(){
        DepositRequestDTO dto = new DepositRequestDTO();
        dto.setAccountNumber("ACC001");
        dto.setAmount(100.0);

        Account acc = new Account();
        acc.setAccountNumber("ACC001");
        acc.setBalance(500.0);

        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(acc));
        when(accountRepository.save(any(Account.class))).thenReturn(acc);

        TransactionResponseDTO responseDTO = accountService.deposit(dto);
        Assertions.assertEquals(600.0,acc.getBalance());

        System.out.println("done with deposit success");
    }


    @Test
    void testDepositInvalidAmount() {
        DepositRequestDTO dto = new DepositRequestDTO();
        dto.setAccountNumber("ACC001");
        dto.setAmount(-10.0);

        Assertions.assertThrows(InvalidAmountException.class, () -> {
            accountService.deposit(dto);
        });
        System.out.println("done with deposit invalid amount");
    }


    @Test
    void testDepositAccountNotFound() {
        DepositRequestDTO dto = new DepositRequestDTO();
        dto.setAccountNumber("XXX");
        dto.setAmount(100.0);

        when(accountRepository.findByAccountNumber("XXX")).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            accountService.deposit(dto);
        });
        System.out.println("done with deposit account not found");
    }

    // ========== WITHDRAW ==========
    @Test
    void testWithdrawSuccess() {
        WithdrawRequestDTO dto = new WithdrawRequestDTO();
        dto.setAccountNumber("ACC001");
        dto.setAmount(200.0);

        Account acc = new Account();
        acc.setAccountNumber("ACC001");
        acc.setBalance(500.0);
//        acc.setTransactions(new ArrayList<>());

        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(acc));
//        when(transactionRepository.save(any(Transactions.class))).thenAnswer(i -> i.getArguments()[0]);

        TransactionResponseDTO response = accountService.withdraw(dto);

        Assertions.assertEquals(300.0, acc.getBalance());
//        Assertions.assertEquals(TransactionType.WITHDRAW, response.getType());
        System.out.println("withdraw success test done");
    }

    @Test
    void testWithdrawInsufficientBalance() {
        WithdrawRequestDTO dto = new WithdrawRequestDTO();
        dto.setAccountNumber("ACC001");
        dto.setAmount(600.0);

        Account acc = new Account();
        acc.setAccountNumber("ACC001");
        acc.setBalance(300.0);

        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(acc));

        Assertions.assertThrows(InsufficientBalanceException.class, () -> {
            accountService.withdraw(dto);
        });
        System.out.println("done with withdraw insufficient balance");
    }

    @Test
    void testWithdrawEmptyAccountNumber(){
        WithdrawRequestDTO dto = new WithdrawRequestDTO();
        dto.setAccountNumber("");
        dto.setAmount(500.0);

        Assertions.assertThrows(InvalidInputException.class,()->{
           accountService.withdraw(dto);
        });

        System.out.println("done with withdraw invalid or null account number");
    }

    @Test
    void testWithdrawEmptyAmount(){
        WithdrawRequestDTO dto = new WithdrawRequestDTO();
        dto.setAccountNumber("ACC001");
        dto.setAmount(0.0);

        Assertions.assertThrows(InvalidAmountException.class,()->{
            accountService.withdraw(dto);
        });

        System.out.println("done with withdraw invalid or null amount");
    }

    @Test
    void testWithdrawAccountNotFound(){
        WithdrawRequestDTO dto = new WithdrawRequestDTO();
        dto.setAccountNumber("ACC001");
        dto.setAmount(500.0);

        when(accountRepository.findByAccountNumber(dto.getAccountNumber()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class,()->{
            accountService.withdraw(dto);
        });

        System.out.println("done with withdraw account not found");
    }



    // ========== TRANSFER ==========
    @Test
    void testTransferSuccess() {
        TransferRequestDTO dto = new TransferRequestDTO();
        dto.setFromAccount("A1");
        dto.setToAccount("A2");
        dto.setAmount(100.0);

        Account acc1 = new Account();
        acc1.setAccountNumber("A1");
        acc1.setBalance(500.0);
//        acc1.setTransactions(new ArrayList<>());

        Account acc2 = new Account();
        acc2.setAccountNumber("A2");
        acc2.setBalance(200.0);
//        acc2.setTransactions(new ArrayList<>());

        when(accountRepository.findByAccountNumber("A1")).thenReturn(Optional.of(acc1));
        when(accountRepository.findByAccountNumber("A2")).thenReturn(Optional.of(acc2));
//        when(transactionRepository.save(any(Transactions.class))).thenAnswer(i -> i.getArguments()[0]);

        TransactionResponseDTO response = accountService.transfer(dto);

        Assertions.assertEquals(400.0, acc1.getBalance());
        Assertions.assertEquals(300.0, acc2.getBalance());
//        Assertions.assertEquals(TransactionType.TRANSFER, response.getType());
        System.out.println("done with transfer success");
    }

    @Test
    void testTransferInsufficientBalance() {
        TransferRequestDTO dto = new TransferRequestDTO();
        dto.setFromAccount("A1");
        dto.setToAccount("A2");
        dto.setAmount(900.0);

        Account acc1 = new Account();
        acc1.setAccountNumber("A1");
        acc1.setBalance(100.0);

        Account acc2 = new Account();
        acc2.setAccountNumber("A2");
        acc2.setBalance(100.0);

        when(accountRepository.findByAccountNumber("A1")).thenReturn(Optional.of(acc1));
        when(accountRepository.findByAccountNumber("A2")).thenReturn(Optional.of(acc2));

        Assertions.assertThrows(InsufficientBalanceException.class, () -> {
            accountService.transfer(dto);
        });

        System.out.println("done with transfer insufficient balance");
    }

    @Test
    void testTransferEmptyAmount(){
        TransferRequestDTO dto = new TransferRequestDTO();
        dto.setFromAccount("A1");
        dto.setToAccount("A2");
        dto.setAmount(0.0);

        Assertions.assertThrows(InvalidAmountException.class,()->{
            accountService.transfer(dto);
        });

        System.out.println("done with transfer empty amount");
    }

    @Test
    void testTransferSourceDestinationSame(){
        TransferRequestDTO dto = new TransferRequestDTO();
        dto.setFromAccount("A1");
        dto.setToAccount("A1");
        dto.setAmount(100.0);

        Assertions.assertThrows(InvalidInputException.class,()->{
            accountService.transfer(dto);
        });

        System.out.println("done with transfer empty amount");
    }

    @Test
    void testTransferSourceAccountNotFound(){
        TransferRequestDTO dto = new TransferRequestDTO();
        dto.setFromAccount("A1");
        dto.setToAccount("A2");
        dto.setAmount(100.0);

        when(accountRepository.findByAccountNumber("A1")).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class,()->{
            accountService.transfer(dto);
        });

        System.out.println("done with transfer source acc not found");
    }

    @Test
    void testTransferDestinationAccountNotFound(){
        TransferRequestDTO dto = new TransferRequestDTO();
        dto.setFromAccount("A1");
        dto.setToAccount("A2");
        dto.setAmount(100.0);

        Account acc1 = new Account();
        acc1.setAccountNumber(dto.getToAccount());
        acc1.setBalance(100.0);

        when(accountRepository.findByAccountNumber("A1")).thenReturn(Optional.of(acc1));
        when(accountRepository.findByAccountNumber("A2")).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class,()->{
            accountService.transfer(dto);
        });

        System.out.println("done with transfer destination acc not found");
    }


    // ========== GET ACCOUNT DETAILS ==========
    @Test
    void testGetAccountDetailsSuccess() {
        Account acc = new Account();
        acc.setAccountNumber("ACC001");
        acc.setHolderName("Kiran");

        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(acc));

        AccountResponseDTO response = accountService.getAccountDetails("ACC001");

        Assertions.assertEquals("Kiran", response.getHolderName());
        Assertions.assertEquals("ACC001", response.getAccountNumber());

        System.out.println("done with account details fetched success");
    }

    @Test
    void testGetAccountDetailsNotFound() {
        when(accountRepository.findByAccountNumber("XXX")).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            accountService.getAccountDetails("XXX");
        });

        System.out.println("done with account details fetched account not found");
    }

    // ========== GET TRANSACTIONS ==========
    @Test
    void testGetTransactionsByAccount() {
        Account acc = new Account();
        acc.setAccountNumber("ACC001");

        Transactions t = new Transactions();
        t.setTransactionId("T1");
        t.setAmount(100.0);
        t.setStatus(TransactionStatus.SUCCESS);
        t.setType(TransactionType.DEPOSIT);

        acc.setTransactions(Arrays.asList(t));

        when(accountRepository.findByAccountNumber("ACC001"))
                .thenReturn(Optional.of(acc));

        List<TransactionResponseDTO> list =
                accountService.getTransactionsByAccount("ACC001");

        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("T1", list.get(0).getTransactionId());

        System.out.println("done with get all transactions");
    }
}
