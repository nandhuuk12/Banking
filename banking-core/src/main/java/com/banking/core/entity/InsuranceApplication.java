package com.banking.core.entity;

import com.banking.core.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing insurance applications
 */
@Entity
@Table(name = "insurance_applications", indexes = {
        @Index(name = "idx_insurance_user", columnList = "user_id"),
        @Index(name = "idx_insurance_policy_number", columnList = "policyNumber", unique = true)
})
public class InsuranceApplication extends BaseEntity {

    @Column(name = "policy_number", unique = true, length = 20)
    private String policyNumber;

    @NotBlank(message = "Policy type is required")
    @Column(name = "policy_type", nullable = false, length = 50)
    private String policyType;

    @NotNull(message = "Premium amount is required")
    @DecimalMin(value = "100.00", message = "Premium amount must be at least 100")
    @Column(name = "premium_amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal premiumAmount;

    @Column(name = "coverage_amount", precision = 19, scale = 2)
    private BigDecimal coverageAmount;

    @Column(name = "term_years")
    private Integer termYears;

    @Column(name = "application_date", nullable = false)
    private LocalDate applicationDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "beneficiary_name", length = 100)
    private String beneficiaryName;

    @Column(name = "beneficiary_relation", length = 50)
    private String beneficiaryRelation;

    @Column(name = "medical_history", length = 1000)
    private String medicalHistory;

    @Column(name = "additional_details", length = 1000)
    private String additionalDetails;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(name = "approval_date")
    private LocalDate approvalDate;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @Column(name = "next_premium_date")
    private LocalDate nextPremiumDate;

    @Column(name = "payment_frequency", length = 20)
    private String paymentFrequency = "MONTHLY";

    // Many-to-one relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public InsuranceApplication() {
        this.applicationDate = LocalDate.now();
    }

    public InsuranceApplication(String policyType, BigDecimal premiumAmount, User user) {
        this();
        this.policyType = policyType;
        this.premiumAmount = premiumAmount;
        this.user = user;
    }

    // Getters and Setters
    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(BigDecimal premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public BigDecimal getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(BigDecimal coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public Integer getTermYears() {
        return termYears;
    }

    public void setTermYears(Integer termYears) {
        this.termYears = termYears;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBeneficiaryRelation() {
        return beneficiaryRelation;
    }

    public void setBeneficiaryRelation(String beneficiaryRelation) {
        this.beneficiaryRelation = beneficiaryRelation;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
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

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDate getNextPremiumDate() {
        return nextPremiumDate;
    }

    public void setNextPremiumDate(LocalDate nextPremiumDate) {
        this.nextPremiumDate = nextPremiumDate;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Utility methods
    public boolean isActive() {
        return status == Status.APPROVED && 
               startDate != null && 
               !LocalDate.now().isBefore(startDate) &&
               (endDate == null || !LocalDate.now().isAfter(endDate));
    }

    public boolean isPremiumDue() {
        return nextPremiumDate != null && !LocalDate.now().isBefore(nextPremiumDate);
    }

    @Override
    public String toString() {
        return "InsuranceApplication{" +
                "id=" + getId() +
                ", policyNumber='" + policyNumber + '\'' +
                ", policyType='" + policyType + '\'' +
                ", status=" + status +
                '}';
    }
}
