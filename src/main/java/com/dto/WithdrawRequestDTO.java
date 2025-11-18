package com.dto;

/*
This DTO is used take accNumber and amount from user for withdraw
 */
public class WithdrawRequestDTO {
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
