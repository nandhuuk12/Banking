package com.banking.core.enums;

/**
 * Enumeration for different types of credit cards
 */
public enum CardType {
    VISA("Visa"),
    MASTERCARD("MasterCard"),
    AMEX("American Express"),
    RUPAY("RuPay");

    private final String displayName;

    CardType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
