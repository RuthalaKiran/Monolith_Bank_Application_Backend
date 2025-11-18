//package com.controller;
//
//import com.dto.AccountRequestDTO;
//import com.dto.AccountResponseDTO;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.service.AccountService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//public class AccountControllerMockTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    private AccountService accountService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void testCreateAccount() throws Exception {
//        AccountRequestDTO req = new AccountRequestDTO();
//        req.setHolderName("Kiran");
//
//        AccountResponseDTO response = new AccountResponseDTO();
//        response.setHolderName("Kiran");
//        response.setAccountNumber("ACC123");
//        response.setCreatedAt(LocalDateTime.now());
//
//        when(accountService.createAccount(any())).thenReturn(response);
//
//        mockMvc.perform(post("/api/accounts/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.holderName").value("Kiran"));
//    }
//
//
//}
