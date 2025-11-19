package com.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/*
This DTO is used take accNumber and amount from user for withdraw
 */
public class WithdrawRequestDTO {
    private String accountNumber;

    @Min(value = 1, message = "Deposit amount must be greater than 0")
    private Double amount;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
