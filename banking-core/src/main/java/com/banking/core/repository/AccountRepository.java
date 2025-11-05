package com.banking.core.repository;

import com.banking.core.entity.Account;
import com.banking.core.entity.User;
import com.banking.core.enums.AccountType;
import com.banking.core.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Account entity
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Find account by account number
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Find accounts by user
     */
    List<Account> findByUser(User user);

    /**
     * Find accounts by user ID
     */
    List<Account> findByUserId(Long userId);

    /**
     * Find accounts by user and account type
     */
    List<Account> findByUserAndAccountType(User user, AccountType accountType);

    /**
     * Find accounts by account type
     */
    List<Account> findByAccountType(AccountType accountType);

    /**
     * Find accounts by status
     */
    List<Account> findByStatus(Status status);

    /**
     * Find accounts by user and status
     */
    List<Account> findByUserAndStatus(User user, Status status);

    /**
     * Find accounts by branch IFSC
     */
    @Query("SELECT a FROM Account a WHERE a.branch.ifsc = :ifsc")
    List<Account> findByBranchIfsc(@Param("ifsc") String ifsc);

    /**
     * Find accounts with balance greater than specified amount
     */
    List<Account> findByBalanceGreaterThan(BigDecimal balance);

    /**
     * Find accounts with balance between specified amounts
     */
    List<Account> findByBalanceBetween(BigDecimal minBalance, BigDecimal maxBalance);

    /**
     * Check if account number exists
     */
    boolean existsByAccountNumber(String accountNumber);

    /**
     * Count accounts by user
     */
    long countByUser(User user);

    /**
     * Count accounts by account type
     */
    long countByAccountType(AccountType accountType);

    /**
     * Get total balance for user's accounts
     */
    @Query("SELECT COALESCE(SUM(a.balance), 0) FROM Account a WHERE a.user = :user AND a.status = :status")
    BigDecimal getTotalBalanceByUser(@Param("user") User user, @Param("status") Status status);

    /**
     * Find active accounts with pagination
     */
    Page<Account> findByStatus(Status status, Pageable pageable);

    /**
     * Find accounts by user with pagination
     */
    Page<Account> findByUser(User user, Pageable pageable);

    /**
     * Find dormant accounts (accounts with no transactions in specified days)
     */
    @Query("SELECT a FROM Account a WHERE a.status = 'ACTIVE' AND a.id NOT IN " +
           "(SELECT DISTINCT t.fromAccount.id FROM Transaction t WHERE t.timestamp >= CURRENT_DATE - :days " +
           "UNION SELECT DISTINCT t.toAccount.id FROM Transaction t WHERE t.timestamp >= CURRENT_DATE - :days)")
    List<Account> findDormantAccounts(@Param("days") int days);
}
