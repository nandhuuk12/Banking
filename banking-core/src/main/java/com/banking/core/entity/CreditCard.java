package com.banking.core.entity;

import com.banking.core.enums.CardType;
import com.banking.core.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing a credit card
 */
@Entity
@Table(name = "credit_cards", indexes = {
        @Index(name = "idx_card_number", columnList = "cardNumber", unique = true),
        @Index(name = "idx_card_user", columnList = "user_id")
})
public class CreditCard extends BaseEntity {

    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must be 16 digits")
    @Column(name = "card_number", unique = true, nullable = false, length = 16)
    private String cardNumber;

    @NotNull(message = "Card type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    @Future(message = "Expiration date must be in the future")
    @NotNull(message = "Expiration date is required")
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @NotBlank(message = "CVV hash is required")
    @Column(name = "cvv_hash", nullable = false)
    private String cvvHash;

    @Column(name = "credit_limit", precision = 19, scale = 2, nullable = false)
    private BigDecimal creditLimit;

    @Column(name = "available_credit", precision = 19, scale = 2, nullable = false)
    private BigDecimal availableCredit;

    @Column(name = "outstanding_balance", precision = 19, scale = 2, nullable = false)
    private BigDecimal outstandingBalance = BigDecimal.ZERO;

    @Column(name = "interest_rate", precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "minimum_payment", precision = 19, scale = 2)
    private BigDecimal minimumPayment = BigDecimal.ZERO;

    @Column(name = "payment_due_date")
    private LocalDate paymentDueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.ACTIVE;

    @Column(name = "pin_hash")
    private String pinHash;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked = false;

    @Column(name = "activation_date")
    private LocalDate activationDate;

    // Many-to-one relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public CreditCard() {}

    public CreditCard(String cardNumber, CardType cardType, LocalDate expirationDate, BigDecimal creditLimit, User user) {
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.expirationDate = expirationDate;
        this.creditLimit = creditLimit;
        this.availableCredit = creditLimit;
        this.user = user;
    }

    // Getters and Setters
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCvvHash() {
        return cvvHash;
    }

    public void setCvvHash(String cvvHash) {
        this.cvvHash = cvvHash;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getAvailableCredit() {
        return availableCredit;
    }

    public void setAvailableCredit(BigDecimal availableCredit) {
        this.availableCredit = availableCredit;
    }

    public BigDecimal getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getMinimumPayment() {
        return minimumPayment;
    }

    public void setMinimumPayment(BigDecimal minimumPayment) {
        this.minimumPayment = minimumPayment;
    }

    public LocalDate getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(LocalDate paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPinHash() {
        return pinHash;
    }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public LocalDate getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Utility methods
    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }

    public boolean canCharge(BigDecimal amount) {
        return !isBlocked && !isExpired() && status == Status.ACTIVE && 
               availableCredit.compareTo(amount) >= 0;
    }

    public void charge(BigDecimal amount) {
        if (!canCharge(amount)) {
            throw new IllegalStateException("Cannot charge this card");
        }
        this.outstandingBalance = this.outstandingBalance.add(amount);
        this.availableCredit = this.availableCredit.subtract(amount);
    }

    public void makePayment(BigDecimal amount) {
        if (amount.compareTo(outstandingBalance) > 0) {
            amount = outstandingBalance;
        }
        this.outstandingBalance = this.outstandingBalance.subtract(amount);
        this.availableCredit = this.availableCredit.add(amount);
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "id=" + getId() +
                ", maskedCardNumber='" + getMaskedCardNumber() + '\'' +
                ", cardType=" + cardType +
                ", status=" + status +
                '}';
    }
}
