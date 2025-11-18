package com.dto;

/*
This DTO is used to take only name from the user
 */
public class AccountRequestDTO {
    private String holderName;

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }
}
