package com.example.simplebank.repos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.example.simplebank.models.Account;

class AccountRepositoryCreationTest {

    private final AccountRepository repository = new AccountRepository();

    @Test
    void createsSavingsAccountForExistingUser() {
        Account account = repository.createAccount(1, "SAVINGS");

        assertNotNull(account);
        assertEquals(13, account.getAccountId());
        assertEquals(account, repository.getAccountById(13));
    }

    @Test
    void rejectsUnknownUserAndUnsupportedType() {
        assertNull(repository.createAccount(999, "SAVINGS"));
        assertNull(repository.createAccount(1, "BUSINESS"));
    }
}