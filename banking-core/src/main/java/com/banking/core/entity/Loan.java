package com.banking.core.entity;

import com.banking.core.enums.LoanType;
import com.banking.core.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing a loan
 */
@Entity
@Table(name = "loans", indexes = {
        @Index(name = "idx_loan_number", columnList = "loanNumber", unique = true),
        @Index(name = "idx_loan_user", columnList = "user_id")
})
public class Loan extends BaseEntity {

    @NotBlank(message = "Loan number is required")
    @Column(name = "loan_number", unique = true, nullable = false, length = 20)
    private String loanNumber;

    @NotNull(message = "Loan type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "loan_type", nullable = false)
    private LoanType loanType;

    @NotNull(message = "Principal amount is required")
    @DecimalMin(value = "1000.00", message = "Principal amount must be at least 1000")
    @Column(name = "principal", precision = 19, scale = 2, nullable = false)
    private BigDecimal principal;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.1", message = "Interest rate must be greater than 0.1%")
    @Column(name = "interest_rate", precision = 5, scale = 2, nullable = false)
    private BigDecimal interestRate;

    @Positive(message = "Term months must be positive")
    @Column(name = "term_months", nullable = false)
    private Integer termMonths;

    @DecimalMin(value = "0.0", message = "Outstanding amount cannot be negative")
    @Column(name = "outstanding_amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal outstandingAmount;

    @Column(name = "emi_amount", precision = 19, scale = 2)
    private BigDecimal emiAmount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "next_payment_date")
    private LocalDate nextPaymentDate;

    @Column(name = "total_payments_made", nullable = false)
    private Integer totalPaymentsMade = 0;

    @Column(name = "late_payment_fee", precision = 19, scale = 2)
    private BigDecimal latePaymentFee = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "purpose", length = 255)
    private String purpose;

    @Column(name = "collateral_details", length = 500)
    private String collateralDetails;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(name = "approval_date")
    private LocalDate approvalDate;

    @Column(name = "disbursed_date")
    private LocalDate disbursedDate;

    @Column(name = "disbursed_amount", precision = 19, scale = 2)
    private BigDecimal disbursedAmount;

    // Many-to-one relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public Loan() {}

    public Loan(String loanNumber, LoanType loanType, BigDecimal principal, BigDecimal interestRate, Integer termMonths, User user) {
        this.loanNumber = loanNumber;
        this.loanType = loanType;
        this.principal = principal;
        this.interestRate = interestRate;
        this.termMonths = termMonths;
        this.outstandingAmount = principal;
        this.user = user;
    }

    // Getters and Setters
    public String getLoanNumber() {
        return loanNumber;
    }

    public void setLoanNumber(String loanNumber) {
        this.loanNumber = loanNumber;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public BigDecimal getEmiAmount() {
        return emiAmount;
    }

    public void setEmiAmount(BigDecimal emiAmount) {
        this.emiAmount = emiAmount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getNextPaymentDate() {
        return nextPaymentDate;
    }

    public void setNextPaymentDate(LocalDate nextPaymentDate) {
        this.nextPaymentDate = nextPaymentDate;
    }

    public Integer getTotalPaymentsMade() {
        return totalPaymentsMade;
    }

    public void setTotalPaymentsMade(Integer totalPaymentsMade) {
        this.totalPaymentsMade = totalPaymentsMade;
    }

    public BigDecimal getLatePaymentFee() {
        return latePaymentFee;
    }

    public void setLatePaymentFee(BigDecimal latePaymentFee) {
        this.latePaymentFee = latePaymentFee;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getCollateralDetails() {
        return collateralDetails;
    }

    public void setCollateralDetails(String collateralDetails) {
        this.collateralDetails = collateralDetails;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDate getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }

    public LocalDate getDisbursedDate() {
        return disbursedDate;
    }

    public void setDisbursedDate(LocalDate disbursedDate) {
        this.disbursedDate = disbursedDate;
    }

    public BigDecimal getDisbursedAmount() {
        return disbursedAmount;
    }

    public void setDisbursedAmount(BigDecimal disbursedAmount) {
        this.disbursedAmount = disbursedAmount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Utility methods
    public boolean isOverdue() {
        return nextPaymentDate != null && LocalDate.now().isAfter(nextPaymentDate);
    }

    public int getRemainingPayments() {
        return Math.max(0, termMonths - totalPaymentsMade);
    }

    public BigDecimal getTotalAmountPayable() {
        if (emiAmount == null) {
            return principal;
        }
        return emiAmount.multiply(BigDecimal.valueOf(termMonths));
    }

    public BigDecimal getTotalInterest() {
        return getTotalAmountPayable().subtract(principal);
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + getId() +
                ", loanNumber='" + loanNumber + '\'' +
                ", loanType=" + loanType +
                ", principal=" + principal +
                ", status=" + status +
                '}';
    }
}
