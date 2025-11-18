package com.utils;

import java.util.Random;

public class AccountNumberGenerator {
    public static String generateAccountNum(String holderName){
//        Auto-generated account number (e.g., initials + random 4 digits)
        String trimmed = holderName.trim().toUpperCase();
        String initial = trimmed.length()>=4? trimmed.substring(0,4) : trimmed;
        int rand = new Random().nextInt(9000)+1000;
        return initial+rand;
    }
}
