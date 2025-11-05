package com.banking.core.exception;

/**
 * Exception thrown when a user is not found
 */
public class UserNotFoundException extends BankingException {

    public UserNotFoundException(String identifier) {
        super(String.format("User not found: %s", identifier), "USER_NOT_FOUND");
    }

    public UserNotFoundException(Long userId) {
        super(String.format("User not found with ID: %d", userId), "USER_NOT_FOUND");
    }
}
