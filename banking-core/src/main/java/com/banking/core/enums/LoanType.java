package com.banking.core.enums;

/**
 * Enumeration for different types of loans
 */
public enum LoanType {
    HOME("Home Loan"),
    AUTO("Auto Loan"),
    PERSONAL("Personal Loan"),
    EDUCATION("Education Loan"),
    BUSINESS("Business Loan");

    private final String displayName;

    LoanType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
