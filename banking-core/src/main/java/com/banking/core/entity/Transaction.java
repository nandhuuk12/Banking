package com.banking.core.entity;

import com.banking.core.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a financial transaction
 */
@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "idx_transaction_txn_id", columnList = "txnId", unique = true),
        @Index(name = "idx_transaction_from_account", columnList = "from_account_id"),
        @Index(name = "idx_transaction_to_account", columnList = "to_account_id"),
        @Index(name = "idx_transaction_timestamp", columnList = "timestamp")
})
public class Transaction extends BaseEntity {

    @NotBlank(message = "Transaction ID is required")
    @Column(name = "txn_id", unique = true, nullable = false, length = 50)
    private String txnId;

    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "txn_type", nullable = false)
    private TransactionType txnType;

    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0.01", message = "Transaction amount must be greater than 0")
    @Column(name = "amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency = "USD";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "narration", length = 255)
    private String narration;

    @Column(name = "reference_number", length = 50)
    private String referenceNumber;

    @Column(name = "balance_after", precision = 19, scale = 2)
    private BigDecimal balanceAfter;

    @Column(name = "fee", precision = 19, scale = 2)
    private BigDecimal fee = BigDecimal.ZERO;

    @Column(name = "processed_by", length = 50)
    private String processedBy;

    // Constructors
    public Transaction() {
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(String txnId, TransactionType txnType, BigDecimal amount, Account fromAccount, Account toAccount, String narration) {
        this();
        this.txnId = txnId;
        this.txnType = txnType;
        this.amount = amount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.narration = narration;
    }

    // Getters and Setters
    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public TransactionType getTxnType() {
        return txnType;
    }

    public void setTxnType(TransactionType txnType) {
        this.txnType = txnType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(String processedBy) {
        this.processedBy = processedBy;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + getId() +
                ", txnId='" + txnId + '\'' +
                ", txnType=" + txnType +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}
