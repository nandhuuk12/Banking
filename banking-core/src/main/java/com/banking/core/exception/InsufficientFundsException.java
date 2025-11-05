package com.banking.core.exception;

import java.math.BigDecimal;

/**
 * Exception thrown when there are insufficient funds for a transaction
 */
public class InsufficientFundsException extends BankingException {

    private final BigDecimal availableBalance;
    private final BigDecimal requestedAmount;

    public InsufficientFundsException(BigDecimal availableBalance, BigDecimal requestedAmount) {
        super(String.format("Insufficient funds. Available: %s, Requested: %s", 
                availableBalance, requestedAmount), "INSUFFICIENT_FUNDS");
        this.availableBalance = availableBalance;
        this.requestedAmount = requestedAmount;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }
}
