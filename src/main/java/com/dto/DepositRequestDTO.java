package com.dto;

/*
This DTO is used to take accNumber and amount from user for deposit
 */
public class DepositRequestDTO {
    private String accountNumber;
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
