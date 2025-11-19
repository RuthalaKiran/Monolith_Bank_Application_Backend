package com.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/*
This DTO is used take source and destination accountNumber and amount
 */
public class TransferRequestDTO {
    @NotBlank(message = "Source account is required")
    private String fromAccount;

    @NotBlank(message = "Destination account is required")
    private String toAccount;

    @Min(value = 1, message = "Transfer amount must be greater than 0")
    private Double amount;

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
