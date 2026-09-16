package com.example.simplebank.controllers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.simplebank.models.Account;
import com.example.simplebank.models.SavingAccount;
import com.example.simplebank.services.AccountService;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    private final Account account = new SavingAccount(11, BigDecimal.ZERO);

    @Test
    void getsAccountById() throws Exception {
        when(accountService.getAccount(11)).thenReturn(account);

        mockMvc.perform(get("/api/accounts/11"))
                .andExpect(status().isOk());
    }

    @Test
    void returnsNotFoundForUnknownAccount() throws Exception {
        when(accountService.getAccount(99)).thenReturn(null);

        mockMvc.perform(get("/api/accounts/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void depositsMoney() throws Exception {
        when(accountService.getAccount(11)).thenReturn(account);
        when(accountService.deposit(eq(11), eq(new BigDecimal("500")))).thenReturn(true);

        mockMvc.perform(post("/api/accounts/11/deposit/500"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.amount").value(500));
    }

    @Test
    void withdrawsMoney() throws Exception {
        when(accountService.getAccount(11)).thenReturn(account);
        when(accountService.withdraw(eq(11), eq(new BigDecimal("200")))).thenReturn(true);

        mockMvc.perform(post("/api/accounts/11/withdraw/200"))
                .andExpect(status().isOk());
    }

        @Test
        void createsAccountFromSingleJsonBody() throws Exception {
        when(accountService.createAccount(1, "SAVINGS")).thenReturn(account);

        String request = objectMapper.writeValueAsString(
            new AccountController.AccountCreationRequest(1, "SAVINGS"));

        mockMvc.perform(post("/api/accounts")
                .contentType("application/json")
                .content(request))
            .andExpect(status().isOk());
        }

    @Test
    void getsTransactionsUsingPathVariable() throws Exception {
        when(accountService.getTransactions(11)).thenReturn(List.of());

        mockMvc.perform(get("/api/accounts/11/transactions"))
                .andExpect(status().isOk());
    }
}