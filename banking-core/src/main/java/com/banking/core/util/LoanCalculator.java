package com.banking.core.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Utility class for loan calculations
 */
public final class LoanCalculator {

    private static final MathContext MATH_CONTEXT = new MathContext(10, RoundingMode.HALF_UP);

    private LoanCalculator() {
        // Utility class
    }

    /**
     * Calculate EMI (Equated Monthly Installment)
     * Formula: EMI = P × r × (1 + r)^n / ((1 + r)^n - 1)
     * Where P = Principal, r = Monthly interest rate, n = Number of months
     */
    public static BigDecimal calculateEMI(BigDecimal principal, BigDecimal annualInterestRate, int termMonths) {
        if (principal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Principal must be positive");
        }
        if (annualInterestRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative");
        }
        if (termMonths <= 0) {
            throw new IllegalArgumentException("Term months must be positive");
        }

        // If interest rate is 0, EMI is simply principal divided by term
        if (annualInterestRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(termMonths), 2, RoundingMode.HALF_UP);
        }

        // Convert annual interest rate to monthly
        BigDecimal monthlyRate = annualInterestRate.divide(BigDecimal.valueOf(100 * 12), MATH_CONTEXT);
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        
        // Calculate (1 + r)^n
        BigDecimal onePlusRPowerN = onePlusR.pow(termMonths, MATH_CONTEXT);
        
        // Calculate EMI
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(onePlusRPowerN);
        BigDecimal denominator = onePlusRPowerN.subtract(BigDecimal.ONE);
        
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

    /**
     * Calculate total amount payable
     */
    public static BigDecimal calculateTotalAmount(BigDecimal emi, int termMonths) {
        return emi.multiply(BigDecimal.valueOf(termMonths));
    }

    /**
     * Calculate total interest
     */
    public static BigDecimal calculateTotalInterest(BigDecimal principal, BigDecimal emi, int termMonths) {
        BigDecimal totalAmount = calculateTotalAmount(emi, termMonths);
        return totalAmount.subtract(principal);
    }

    /**
     * Calculate outstanding balance after certain payments
     */
    public static BigDecimal calculateOutstandingBalance(BigDecimal principal, BigDecimal annualInterestRate, 
                                                         int termMonths, int paymentsMade) {
        if (paymentsMade >= termMonths) {
            return BigDecimal.ZERO;
        }

        BigDecimal emi = calculateEMI(principal, annualInterestRate, termMonths);
        BigDecimal monthlyRate = annualInterestRate.divide(BigDecimal.valueOf(100 * 12), MATH_CONTEXT);
        
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRPowerN = onePlusR.pow(termMonths, MATH_CONTEXT);
        BigDecimal onePlusRPowerP = onePlusR.pow(paymentsMade, MATH_CONTEXT);
        
        BigDecimal numerator = emi.multiply(onePlusRPowerN.subtract(onePlusRPowerP));
        BigDecimal denominator = monthlyRate.multiply(onePlusRPowerN);
        
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

    /**
     * Calculate maximum loan amount based on EMI capacity
     */
    public static BigDecimal calculateMaxLoanAmount(BigDecimal maxEMI, BigDecimal annualInterestRate, int termMonths) {
        if (annualInterestRate.compareTo(BigDecimal.ZERO) == 0) {
            return maxEMI.multiply(BigDecimal.valueOf(termMonths));
        }

        BigDecimal monthlyRate = annualInterestRate.divide(BigDecimal.valueOf(100 * 12), MATH_CONTEXT);
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRPowerN = onePlusR.pow(termMonths, MATH_CONTEXT);
        
        BigDecimal numerator = maxEMI.multiply(onePlusRPowerN.subtract(BigDecimal.ONE));
        BigDecimal denominator = monthlyRate.multiply(onePlusRPowerN);
        
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}
