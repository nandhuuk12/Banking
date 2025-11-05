package com.banking.core.repository;

import com.banking.core.entity.CreditCard;
import com.banking.core.entity.User;
import com.banking.core.enums.CardType;
import com.banking.core.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CreditCard entity
 */
@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    /**
     * Find credit card by card number
     */
    Optional<CreditCard> findByCardNumber(String cardNumber);

    /**
     * Find credit cards by user
     */
    List<CreditCard> findByUser(User user);

    /**
     * Find credit cards by user ID
     */
    List<CreditCard> findByUserId(Long userId);

    /**
     * Find credit cards by user and status
     */
    List<CreditCard> findByUserAndStatus(User user, Status status);

    /**
     * Find credit cards by card type
     */
    List<CreditCard> findByCardType(CardType cardType);

    /**
     * Find credit cards by status
     */
    List<CreditCard> findByStatus(Status status);

    /**
     * Find blocked credit cards
     */
    List<CreditCard> findByIsBlockedTrue();

    /**
     * Find active (unblocked) credit cards
     */
    List<CreditCard> findByIsBlockedFalse();

    /**
     * Find expired credit cards
     */
    List<CreditCard> findByExpirationDateBefore(LocalDate date);

    /**
     * Find credit cards expiring soon
     */
    List<CreditCard> findByExpirationDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find credit cards with outstanding balance
     */
    List<CreditCard> findByOutstandingBalanceGreaterThan(BigDecimal amount);

    /**
     * Find credit cards with high utilization
     */
    @Query("SELECT c FROM CreditCard c WHERE " +
           "(c.outstandingBalance / c.creditLimit) > :utilizationRatio")
    List<CreditCard> findHighUtilizationCards(@Param("utilizationRatio") BigDecimal utilizationRatio);

    /**
     * Check if card number exists
     */
    boolean existsByCardNumber(String cardNumber);

    /**
     * Count credit cards by user
     */
    long countByUser(User user);

    /**
     * Count credit cards by status
     */
    long countByStatus(Status status);

    /**
     * Get total credit limit for user
     */
    @Query("SELECT COALESCE(SUM(c.creditLimit), 0) FROM CreditCard c WHERE c.user = :user AND c.status = :status")
    BigDecimal getTotalCreditLimitByUser(@Param("user") User user, @Param("status") Status status);

    /**
     * Get total outstanding balance for user
     */
    @Query("SELECT COALESCE(SUM(c.outstandingBalance), 0) FROM CreditCard c WHERE c.user = :user")
    BigDecimal getTotalOutstandingByUser(@Param("user") User user);

    /**
     * Find cards due for payment
     */
    List<CreditCard> findByPaymentDueDateBefore(LocalDate date);

    /**
     * Find cards with overdue payments
     */
    @Query("SELECT c FROM CreditCard c WHERE c.paymentDueDate < CURRENT_DATE AND c.outstandingBalance > 0")
    List<CreditCard> findOverdueCards();

    /**
     * Find recently issued cards
     */
    List<CreditCard> findByActivationDateAfter(LocalDate date);
}
