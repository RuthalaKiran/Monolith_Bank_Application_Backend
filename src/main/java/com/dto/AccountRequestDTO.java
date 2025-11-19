package com.dto;

import jakarta.validation.constraints.NotNull;

/*
This DTO is used to take only name from the user
 */
public class AccountRequestDTO {
    @NotNull(message = "Account holder name is required")
    private String holderName;

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }
}
