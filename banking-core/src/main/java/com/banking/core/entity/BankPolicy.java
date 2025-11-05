package com.banking.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Entity representing bank policies
 */
@Entity
@Table(name = "bank_policies", indexes = {
        @Index(name = "idx_policy_code", columnList = "policyCode", unique = true)
})
public class BankPolicy extends BaseEntity {

    @NotBlank(message = "Policy code is required")
    @Column(name = "policy_code", unique = true, nullable = false, length = 20)
    private String policyCode;

    @NotBlank(message = "Policy title is required")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @NotBlank(message = "Policy description is required")
    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @NotNull(message = "Effective date is required")
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "policy_version", length = 10)
    private String policyVersion;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    // Constructors
    public BankPolicy() {}

    public BankPolicy(String policyCode, String title, String description, LocalDate effectiveDate) {
        this.policyCode = policyCode;
        this.title = title;
        this.description = description;
        this.effectiveDate = effectiveDate;
    }

    // Getters and Setters
    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPolicyVersion() {
        return policyVersion;
    }

    public void setPolicyVersion(String policyVersion) {
        this.policyVersion = policyVersion;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    // Utility methods
    public boolean isCurrentlyEffective() {
        LocalDate now = LocalDate.now();
        return isActive && 
               !now.isBefore(effectiveDate) && 
               (expiryDate == null || !now.isAfter(expiryDate));
    }

    @Override
    public String toString() {
        return "BankPolicy{" +
                "id=" + getId() +
                ", policyCode='" + policyCode + '\'' +
                ", title='" + title + '\'' +
                ", effectiveDate=" + effectiveDate +
                ", isActive=" + isActive +
                '}';
    }
}
