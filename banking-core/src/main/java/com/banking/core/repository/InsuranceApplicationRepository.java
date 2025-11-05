package com.banking.core.repository;

import com.banking.core.entity.InsuranceApplication;
import com.banking.core.entity.User;
import com.banking.core.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for InsuranceApplication entity
 */
@Repository
public interface InsuranceApplicationRepository extends JpaRepository<InsuranceApplication, Long> {

    /**
     * Find insurance application by policy number
     */
    Optional<InsuranceApplication> findByPolicyNumber(String policyNumber);

    /**
     * Find insurance applications by user
     */
    List<InsuranceApplication> findByUser(User user);

    /**
     * Find insurance applications by user ID
     */
    List<InsuranceApplication> findByUserId(Long userId);

    /**
     * Find insurance applications by user and status
     */
    List<InsuranceApplication> findByUserAndStatus(User user, Status status);

    /**
     * Find insurance applications by policy type
     */
    List<InsuranceApplication> findByPolicyType(String policyType);

    /**
     * Find insurance applications by status
     */
    List<InsuranceApplication> findByStatus(Status status);

    /**
     * Find insurance applications by status with pagination
     */
    Page<InsuranceApplication> findByStatus(Status status, Pageable pageable);

    /**
     * Find pending applications
     */
    List<InsuranceApplication> findByStatusOrderByApplicationDateAsc(Status status);

    /**
     * Find active insurance policies
     */
    @Query("SELECT i FROM InsuranceApplication i WHERE i.status = 'APPROVED' " +
           "AND i.startDate <= CURRENT_DATE " +
           "AND (i.endDate IS NULL OR i.endDate > CURRENT_DATE)")
    List<InsuranceApplication> findActivePolicies();

    /**
     * Find policies with premium due
     */
    List<InsuranceApplication> findByNextPremiumDateBefore(LocalDate date);

    /**
     * Find overdue premium payments
     */
    @Query("SELECT i FROM InsuranceApplication i WHERE i.nextPremiumDate < CURRENT_DATE " +
           "AND i.status = 'APPROVED'")
    List<InsuranceApplication> findOverduePremiums();

    /**
     * Find applications by premium amount range
     */
    List<InsuranceApplication> findByPremiumAmountBetween(BigDecimal minPremium, BigDecimal maxPremium);

    /**
     * Find applications by coverage amount
     */
    List<InsuranceApplication> findByCoverageAmountGreaterThan(BigDecimal coverageAmount);

    /**
     * Find applications by application date range
     */
    List<InsuranceApplication> findByApplicationDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find recently approved policies
     */
    List<InsuranceApplication> findByStatusAndApprovalDateAfter(Status status, LocalDate date);

    /**
     * Check if policy number exists
     */
    boolean existsByPolicyNumber(String policyNumber);

    /**
     * Count applications by user
     */
    long countByUser(User user);

    /**
     * Count applications by status
     */
    long countByStatus(Status status);

    /**
     * Count applications by policy type
     */
    long countByPolicyType(String policyType);

    /**
     * Get total premium amount by user
     */
    @Query("SELECT COALESCE(SUM(i.premiumAmount), 0) FROM InsuranceApplication i " +
           "WHERE i.user = :user AND i.status = 'APPROVED'")
    BigDecimal getTotalPremiumByUser(@Param("user") User user);

    /**
     * Get total coverage amount by user
     */
    @Query("SELECT COALESCE(SUM(i.coverageAmount), 0) FROM InsuranceApplication i " +
           "WHERE i.user = :user AND i.status = 'APPROVED'")
    BigDecimal getTotalCoverageByUser(@Param("user") User user);

    /**
     * Find applications approved by specific person
     */
    List<InsuranceApplication> findByApprovedBy(String approvedBy);

    /**
     * Find policies expiring soon
     */
    List<InsuranceApplication> findByEndDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Get insurance summary by policy type
     */
    @Query("SELECT i.policyType, COUNT(i), SUM(i.premiumAmount), SUM(i.coverageAmount) " +
           "FROM InsuranceApplication i WHERE i.status = 'APPROVED' GROUP BY i.policyType")
    List<Object[]> getInsuranceSummaryByType();

    /**
     * Find high-value policies
     */
    @Query("SELECT i FROM InsuranceApplication i WHERE i.coverageAmount > :threshold " +
           "ORDER BY i.coverageAmount DESC")
    List<InsuranceApplication> findHighValuePolicies(@Param("threshold") BigDecimal threshold);

    /**
     * Find policies by payment frequency
     */
    List<InsuranceApplication> findByPaymentFrequency(String paymentFrequency);
}
