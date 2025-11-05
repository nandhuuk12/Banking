package com.banking.core.exception;

/**
 * Exception thrown when a transaction fails
 */
public class TransactionFailedException extends BankingException {

    public TransactionFailedException(String message) {
        super(message, "TRANSACTION_FAILED");
    }

    public TransactionFailedException(String message, Throwable cause) {
        super(message, "TRANSACTION_FAILED", cause);
    }
}
