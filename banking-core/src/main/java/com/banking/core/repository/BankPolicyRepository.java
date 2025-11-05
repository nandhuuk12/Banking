package com.banking.core.repository;

import com.banking.core.entity.BankPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for BankPolicy entity
 */
@Repository
public interface BankPolicyRepository extends JpaRepository<BankPolicy, Long> {

    /**
     * Find policy by policy code
     */
    Optional<BankPolicy> findByPolicyCode(String policyCode);

    /**
     * Find policies by category
     */
    List<BankPolicy> findByCategoryIgnoreCase(String category);

    /**
     * Find active policies
     */
    List<BankPolicy> findByIsActiveTrue();

    /**
     * Find inactive policies
     */
    List<BankPolicy> findByIsActiveFalse();

    /**
     * Find currently effective policies
     */
    @Query("SELECT p FROM BankPolicy p WHERE p.isActive = true " +
           "AND p.effectiveDate <= CURRENT_DATE " +
           "AND (p.expiryDate IS NULL OR p.expiryDate > CURRENT_DATE)")
    List<BankPolicy> findCurrentlyEffectivePolicies();

    /**
     * Find policies by effective date range
     */
    List<BankPolicy> findByEffectiveDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find policies expiring soon
     */
    List<BankPolicy> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find expired policies
     */
    List<BankPolicy> findByExpiryDateBefore(LocalDate date);

    /**
     * Find policies by title containing text
     */
    List<BankPolicy> findByTitleContainingIgnoreCase(String title);

    /**
     * Find policies by version
     */
    List<BankPolicy> findByPolicyVersion(String policyVersion);

    /**
     * Find policies created by specific person
     */
    List<BankPolicy> findByCreatedBy(String createdBy);

    /**
     * Find policies approved by specific person
     */
    List<BankPolicy> findByApprovedBy(String approvedBy);

    /**
     * Check if policy code exists
     */
    boolean existsByPolicyCode(String policyCode);

    /**
     * Count active policies
     */
    long countByIsActiveTrue();

    /**
     * Count policies by category
     */
    long countByCategoryIgnoreCase(String category);

    /**
     * Find policies effective on specific date
     */
    @Query("SELECT p FROM BankPolicy p WHERE p.isActive = true " +
           "AND p.effectiveDate <= :date " +
           "AND (p.expiryDate IS NULL OR p.expiryDate > :date)")
    List<BankPolicy> findPoliciesEffectiveOn(@Param("date") LocalDate date);

    /**
     * Get policy categories
     */
    @Query("SELECT DISTINCT p.category FROM BankPolicy p WHERE p.category IS NOT NULL ORDER BY p.category")
    List<String> findDistinctCategories();

    /**
     * Get policy summary by category
     */
    @Query("SELECT p.category, COUNT(p) FROM BankPolicy p WHERE p.isActive = true GROUP BY p.category")
    List<Object[]> getPolicySummaryByCategory();

    /**
     * Find latest version of policies by code pattern
     */
    @Query("SELECT p FROM BankPolicy p WHERE p.policyCode LIKE :codePattern " +
           "ORDER BY p.policyVersion DESC, p.effectiveDate DESC")
    List<BankPolicy> findLatestVersionsByCodePattern(@Param("codePattern") String codePattern);
}
