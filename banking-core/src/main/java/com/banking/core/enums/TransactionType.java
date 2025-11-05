package com.banking.core.enums;

/**
 * Enumeration for different types of transactions
 */
public enum TransactionType {
    DEBIT("Debit"),
    CREDIT("Credit"),
    TRANSFER("Transfer"),
    WITHDRAWAL("Withdrawal"),
    DEPOSIT("Deposit"),
    FEE("Fee"),
    INTEREST("Interest");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
