package com.banking.core.exception;

/**
 * Exception thrown when an account is not found
 */
public class AccountNotFoundException extends BankingException {

    public AccountNotFoundException(String accountNumber) {
        super(String.format("Account not found: %s", accountNumber), "ACCOUNT_NOT_FOUND");
    }

    public AccountNotFoundException(Long accountId) {
        super(String.format("Account not found with ID: %d", accountId), "ACCOUNT_NOT_FOUND");
    }
}
