package com.banking.core.repository;

import com.banking.core.entity.Loan;
import com.banking.core.entity.User;
import com.banking.core.enums.LoanType;
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
 * Repository interface for Loan entity
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    /**
     * Find loan by loan number
     */
    Optional<Loan> findByLoanNumber(String loanNumber);

    /**
     * Find loans by user
     */
    List<Loan> findByUser(User user);

    /**
     * Find loans by user ID
     */
    List<Loan> findByUserId(Long userId);

    /**
     * Find loans by user and status
     */
    List<Loan> findByUserAndStatus(User user, Status status);

    /**
     * Find loans by loan type
     */
    List<Loan> findByLoanType(LoanType loanType);

    /**
     * Find loans by status
     */
    List<Loan> findByStatus(Status status);

    /**
     * Find loans by status with pagination
     */
    Page<Loan> findByStatus(Status status, Pageable pageable);

    /**
     * Find active loans
     */
    @Query("SELECT l FROM Loan l WHERE l.status IN ('APPROVED', 'ACTIVE') AND l.outstandingAmount > 0")
    List<Loan> findActiveLoans();

    /**
     * Find overdue loans
     */
    @Query("SELECT l FROM Loan l WHERE l.nextPaymentDate < CURRENT_DATE AND l.outstandingAmount > 0")
    List<Loan> findOverdueLoans();

    /**
     * Find loans with payments due
     */
    List<Loan> findByNextPaymentDateBefore(LocalDate date);

    /**
     * Find loans by principal amount range
     */
    List<Loan> findByPrincipalBetween(BigDecimal minPrincipal, BigDecimal maxPrincipal);

    /**
     * Find loans by outstanding amount
     */
    List<Loan> findByOutstandingAmountGreaterThan(BigDecimal amount);

    /**
     * Find recently disbursed loans
     */
    List<Loan> findByDisbursedDateAfter(LocalDate date);

    /**
     * Check if loan number exists
     */
    boolean existsByLoanNumber(String loanNumber);

    /**
     * Count loans by user
     */
    long countByUser(User user);

    /**
     * Count loans by status
     */
    long countByStatus(Status status);

    /**
     * Count loans by loan type
     */
    long countByLoanType(LoanType loanType);

    /**
     * Get total outstanding amount for user
     */
    @Query("SELECT COALESCE(SUM(l.outstandingAmount), 0) FROM Loan l WHERE l.user = :user AND l.status IN ('APPROVED', 'ACTIVE')")
    BigDecimal getTotalOutstandingByUser(@Param("user") User user);

    /**
     * Get total disbursed amount by loan type
     */
    @Query("SELECT COALESCE(SUM(l.disbursedAmount), 0) FROM Loan l WHERE l.loanType = :loanType AND l.status = 'APPROVED'")
    BigDecimal getTotalDisbursedByType(@Param("loanType") LoanType loanType);

    /**
     * Find loans approved by specific person
     */
    List<Loan> findByApprovedBy(String approvedBy);

    /**
     * Find loans by term months
     */
    List<Loan> findByTermMonths(Integer termMonths);

    /**
     * Find loans ending soon
     */
    List<Loan> findByEndDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Get loan portfolio summary
     */
    @Query("SELECT l.loanType, COUNT(l), SUM(l.principal), SUM(l.outstandingAmount) " +
           "FROM Loan l WHERE l.status IN ('APPROVED', 'ACTIVE') GROUP BY l.loanType")
    List<Object[]> getLoanPortfolioSummary();

    /**
     * Find high-value loans
     */
    @Query("SELECT l FROM Loan l WHERE l.principal > :threshold ORDER BY l.principal DESC")
    List<Loan> findHighValueLoans(@Param("threshold") BigDecimal threshold);
}
