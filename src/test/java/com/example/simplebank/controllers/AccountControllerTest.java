package com.example.simplebank.controllers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.simplebank.models.Account;
import com.example.simplebank.models.SavingAccount;
import com.example.simplebank.security.JwtService;
import com.example.simplebank.security.SecurityConfig;
import com.example.simplebank.services.AccountService;

// the real JWT filter chain stays active here; requests authenticate via the
// SecurityMockMvcRequestPostProcessor instead of a real Bearer token
@WebMvcTest(AccountController.class)
@Import(SecurityConfig.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    // unused directly, but required to satisfy JwtAuthenticationFilter's constructor in this slice
    @MockBean
    private JwtService jwtService;

    private final Account account = new SavingAccount(11, BigDecimal.ZERO);

    private static RequestPostProcessor asUser(int userId) {
        return authentication(new UsernamePasswordAuthenticationToken(userId, null, List.of()));
    }

    @Test
    void getsAccountById() throws Exception {
        when(accountService.getAccount(11)).thenReturn(account);
        when(accountService.getOwnerUserId(11)).thenReturn(1);

        mockMvc.perform(get("/api/accounts/11").with(asUser(1)))
                .andExpect(status().isOk());
    }

    @Test
    void returnsNotFoundForUnknownAccount() throws Exception {
        when(accountService.getAccount(99)).thenReturn(null);
        when(accountService.getOwnerUserId(99)).thenReturn(1);

        mockMvc.perform(get("/api/accounts/99").with(asUser(1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void returnsForbiddenForAccountOwnedByAnotherUser() throws Exception {
        when(accountService.getOwnerUserId(11)).thenReturn(1);

        mockMvc.perform(get("/api/accounts/11").with(asUser(2)))
                .andExpect(status().isForbidden());
    }

    @Test
    void depositsMoney() throws Exception {
        when(accountService.getOwnerUserId(11)).thenReturn(1);
        when(accountService.deposit(eq(11), eq(new BigDecimal("500")))).thenReturn(true);

        mockMvc.perform(put("/api/accounts/11/deposit")
            .with(asUser(1))
            .contentType("application/json")
            .content("{\"amount\":500}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.amount").value(500));
    }

    @Test
    void withdrawsMoney() throws Exception {
        when(accountService.getOwnerUserId(11)).thenReturn(1);
        when(accountService.withdraw(eq(11), eq(new BigDecimal("200")))).thenReturn(true);

        mockMvc.perform(put("/api/accounts/11/withdraw")
            .with(asUser(1))
            .contentType("application/json")
            .content("{\"amount\":200}"))
                .andExpect(status().isOk());
    }

    @Test
    void createsAccountFromSingleJsonBody() throws Exception {
        when(accountService.createAccount(1, "SAVINGS")).thenReturn(account);

        String request = objectMapper.writeValueAsString(
            new AccountController.AccountCreationRequest(1, "SAVINGS"));

        mockMvc.perform(post("/api/accounts")
                .with(asUser(1))
                .contentType("application/json")
                .content(request))
            .andExpect(status().isOk());
    }

    @Test
    void getsTransactionsUsingPathVariable() throws Exception {
        when(accountService.getOwnerUserId(11)).thenReturn(1);
        when(accountService.getTransactions(11)).thenReturn(List.of());

        mockMvc.perform(get("/api/accounts/11/transactions").with(asUser(1)))
                .andExpect(status().isOk());
    }
}
