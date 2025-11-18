package com.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TransactionIdGenerator {
    public static String generateTransactionId(){
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        int rand = new Random().nextInt(999)+1;
        return "TXN-"+date+"-"+rand;
    }
}
