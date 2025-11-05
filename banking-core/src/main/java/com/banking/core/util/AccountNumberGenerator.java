package com.banking.core.util;

import com.banking.core.enums.AccountType;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for generating account numbers
 */
public final class AccountNumberGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final String BANK_CODE = "1234"; // Bank identifier

    private AccountNumberGenerator() {
        // Utility class
    }

    /**
     * Generate account number based on account type
     */
    public static String generateAccountNumber(AccountType accountType) {
        String typeCode = getTypeCode(accountType);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
        String randomDigits = String.format("%04d", random.nextInt(10000));
        
        return BANK_CODE + typeCode + timestamp + randomDigits;
    }

    /**
     * Generate transaction ID
     */
    public static String generateTransactionId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmssSSS"));
        String randomDigits = String.format("%06d", random.nextInt(1000000));
        
        return "TXN" + timestamp + randomDigits;
    }

    /**
     * Generate loan number
     */
    public static String generateLoanNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMM"));
        String randomDigits = String.format("%08d", random.nextInt(100000000));
        
        return "LN" + timestamp + randomDigits;
    }

    /**
     * Generate credit card number (16 digits)
     */
    public static String generateCreditCardNumber() {
        // Using a simplified approach - in production, use proper Luhn algorithm
        StringBuilder cardNumber = new StringBuilder();
        cardNumber.append("4"); // Visa prefix
        
        for (int i = 1; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
        }
        
        return cardNumber.toString();
    }

    /**
     * Generate policy number for insurance
     */
    public static String generatePolicyNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMM"));
        String randomDigits = String.format("%08d", random.nextInt(100000000));
        
        return "POL" + timestamp + randomDigits;
    }

    private static String getTypeCode(AccountType accountType) {
        switch (accountType) {
            case SAVINGS:
                return "01";
            case CURRENT:
                return "02";
            case SALARY:
                return "03";
            case FIXED_DEPOSIT:
                return "04";
            default:
                return "99";
        }
    }
}
