package com.banking.core.service;

import com.banking.core.entity.Account;
import com.banking.core.entity.User;
import com.banking.core.enums.AccountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Account operations
 */
public interface AccountService {

    /**
     * Create a new account for user
     */
    Account createAccount(User user, AccountType accountType, String branchIfsc);

    /**
     * Create account with initial deposit
     */
    Account createAccount(User user, AccountType accountType, String branchIfsc, BigDecimal initialDeposit);

    /**
     * Find account by ID
     */
    Optional<Account> findById(Long accountId);

    /**
     * Find account by account number
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Find accounts by user
     */
    List<Account> findByUser(User user);

    /**
     * Find accounts by user with pagination
     */
    Page<Account> findByUser(User user, Pageable pageable);

    /**
     * Deposit money to account
     */
    Account deposit(String accountNumber, BigDecimal amount, String narration);

    /**
     * Withdraw money from account
     */
    Account withdraw(String accountNumber, BigDecimal amount, String narration);

    /**
     * Transfer money between accounts
     */
    void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String narration);

    /**
     * Get account balance
     */
    BigDecimal getBalance(String accountNumber);

    /**
     * Get available balance (including overdraft)
     */
    BigDecimal getAvailableBalance(String accountNumber);

    /**
     * Check if account can withdraw amount
     */
    boolean canWithdraw(String accountNumber, BigDecimal amount);

    /**
     * Close account
     */
    void closeAccount(String accountNumber, String reason);

    /**
     * Activate account
     */
    void activateAccount(String accountNumber);

    /**
     * Block account
     */
    void blockAccount(String accountNumber, String reason);

    /**
     * Unblock account
     */
    void unblockAccount(String accountNumber);

    /**
     * Update overdraft limit
     */
    void updateOverdraftLimit(String accountNumber, BigDecimal overdraftLimit);

    /**
     * Get total balance for user
     */
    BigDecimal getTotalBalance(User user);

    /**
     * Find dormant accounts
     */
    List<Account> findDormantAccounts(int daysSinceLastTransaction);

    /**
     * Get account summary
     */
    Object getAccountSummary(User user);
}
