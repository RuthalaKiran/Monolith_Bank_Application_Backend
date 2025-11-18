package com.controller;

import com.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@SpringBootApplication(scanBasePackages = "com")
public class AccountControllerTest {

    @Test
    void testCreateAccountUsingRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/api/accounts/create";

        AccountRequestDTO dto = new AccountRequestDTO();
        dto.setHolderName("Kiran");

        // Call API
        ApiResponse response =
                restTemplate.postForObject(url, dto, ApiResponse.class);

        System.out.println("response is "+response);

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isSuccess());

        System.out.println("create account api is done");
    }

    @Test
    void testDepositUsingRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/api/accounts/{accNo}/deposit";

        DepositRequestDTO dto = new DepositRequestDTO();
        dto.setAccountNumber("KIRA8018");
        dto.setAmount(50000.0);

        // Call API
        ResponseEntity<ApiResponse> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.PUT,
                        new HttpEntity<>(dto),
                        ApiResponse.class,
                        "KIRA8018"
                );
        System.out.println("response is "+response);

        System.out.println(response.getBody());

        ApiResponse body = response.getBody();

       Assertions.assertNotNull(body);
       Assertions.assertTrue(body.isSuccess());
       Assertions.assertEquals("Deposit successfully",body.getMessage());

        System.out.println("create account api is done");
    }

    @Test
    void testDepositFailUsingRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false; //  dont throw 400/500 exceptions
            }
        });
        String url = "http://localhost:8080/api/accounts/{accNo}/deposit";

        DepositRequestDTO dto = new DepositRequestDTO();
        dto.setAccountNumber("KIRA8018");
        dto.setAmount(0.0);

        // Call API
        ResponseEntity<ApiResponse> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.PUT,
                        new HttpEntity<>(dto),
                        ApiResponse.class,
                        "KIRA8018"
                );
        System.out.println("response is "+response);

        System.out.println(response.getBody());

        ApiResponse body = response.getBody();

        Assertions.assertNotNull(body);
        Assertions.assertFalse(body.isSuccess());
        Assertions.assertEquals("Amount must be greater than 0",body.getMessage());

        System.out.println("create account api is done");
    }

    @Test
    void testWithdrawUsingRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/api/accounts/{accNo}/withdraw";

        WithdrawRequestDTO dto = new WithdrawRequestDTO();
        dto.setAccountNumber("KIRA8018");
        dto.setAmount(500.0);

        // CALL API
        ResponseEntity<ApiResponse> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.PUT,
                        new HttpEntity<>(dto),
                        ApiResponse.class,
                        "KIRA8018"
                );

        System.out.println("response is "+response);
        ApiResponse body = response.getBody();

        Assertions.assertNotNull(body);
        Assertions.assertTrue(body.isSuccess());
        Assertions.assertEquals("Withdrawal successfully",body.getMessage());
    }

    @Test
    void testTransferUsingRestTemplate(){
        TransferRequestDTO dto = new TransferRequestDTO();
        dto.setFromAccount("KIRA8018");
        dto.setToAccount("KIRA8550");
        dto.setAmount(100.0);

        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/api/accounts/transfer";

        ApiResponse response = restTemplate.postForObject(url,dto,ApiResponse.class);

        System.out.println("response is "+response);

        Assertions.assertNotNull(response);

        Assertions.assertEquals("Amount Transferred Successfully",response.getMessage());
    }

    @Test
    void testGetAccountUsingRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/accounts/{accountNumber}";
        ApiResponse response = restTemplate.getForObject(url,ApiResponse.class,"KIRA8550");

        System.out.println("response is "+response);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Successfully fetched account details",response.getMessage());
    }

    @Test
    void testGetTransactionsByAccount(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/accounts/{accountNumber}/transactions";

        ResponseEntity<ApiResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                ApiResponse.class,
                "KIRA8550"
        );

        System.out.println("response is "+response);
        ApiResponse res = response.getBody();

        Assertions.assertNotNull(res);
        Assertions.assertEquals("Successfully fetched transactions",res.getMessage());
    }

}
