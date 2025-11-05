package com.banking.core.repository;

import com.banking.core.entity.Account;
import com.banking.core.entity.Transaction;
import com.banking.core.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Transaction entity
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Find transaction by transaction ID
     */
    Optional<Transaction> findByTxnId(String txnId);

    /**
     * Find transactions by from account
     */
    List<Transaction> findByFromAccount(Account fromAccount);

    /**
     * Find transactions by to account
     */
    List<Transaction> findByToAccount(Account toAccount);

    /**
     * Find transactions by account (either from or to)
     */
    @Query("SELECT t FROM Transaction t WHERE t.fromAccount = :account OR t.toAccount = :account")
    List<Transaction> findByAccount(@Param("account") Account account);

    /**
     * Find transactions by account with pagination
     */
    @Query("SELECT t FROM Transaction t WHERE t.fromAccount = :account OR t.toAccount = :account ORDER BY t.timestamp DESC")
    Page<Transaction> findByAccount(@Param("account") Account account, Pageable pageable);

    /**
     * Find transactions by transaction type
     */
    List<Transaction> findByTxnType(TransactionType txnType);

    /**
     * Find transactions between dates
     */
    List<Transaction> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find transactions by account and date range
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount = :account OR t.toAccount = :account) " +
           "AND t.timestamp BETWEEN :startDate AND :endDate ORDER BY t.timestamp DESC")
    List<Transaction> findByAccountAndDateRange(@Param("account") Account account,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

    /**
     * Find transactions by account, type and date range
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount = :account OR t.toAccount = :account) " +
           "AND t.txnType = :txnType AND t.timestamp BETWEEN :startDate AND :endDate ORDER BY t.timestamp DESC")
    List<Transaction> findByAccountTypeAndDateRange(@Param("account") Account account,
                                                    @Param("txnType") TransactionType txnType,
                                                    @Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    /**
     * Find transactions with amount greater than specified value
     */
    List<Transaction> findByAmountGreaterThan(BigDecimal amount);

    /**
     * Find large transactions (above threshold) in date range
     */
    @Query("SELECT t FROM Transaction t WHERE t.amount > :threshold AND t.timestamp BETWEEN :startDate AND :endDate")
    List<Transaction> findLargeTransactions(@Param("threshold") BigDecimal threshold,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);

    /**
     * Get transaction count by account
     */
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.fromAccount = :account OR t.toAccount = :account")
    long countByAccount(@Param("account") Account account);

    /**
     * Get total transaction amount by account and type
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE " +
           "(t.fromAccount = :account OR t.toAccount = :account) AND t.txnType = :txnType")
    BigDecimal getTotalAmountByAccountAndType(@Param("account") Account account,
                                              @Param("txnType") TransactionType txnType);

    /**
     * Check if transaction ID exists
     */
    boolean existsByTxnId(String txnId);

    /**
     * Find recent transactions for account
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount = :account OR t.toAccount = :account) " +
           "ORDER BY t.timestamp DESC")
    Page<Transaction> findRecentTransactions(@Param("account") Account account, Pageable pageable);

    /**
     * Find transactions by reference number
     */
    List<Transaction> findByReferenceNumber(String referenceNumber);

    /**
     * Find daily transaction summary
     */
    @Query("SELECT DATE(t.timestamp) as date, t.txnType, COUNT(t), SUM(t.amount) " +
           "FROM Transaction t WHERE t.timestamp BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(t.timestamp), t.txnType")
    List<Object[]> getDailyTransactionSummary(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);
}
