package com.banking.core.service.impl;

import com.banking.core.entity.Account;
import com.banking.core.entity.Branch;
import com.banking.core.entity.Transaction;
import com.banking.core.entity.User;
import com.banking.core.enums.AccountType;
import com.banking.core.enums.Status;
import com.banking.core.enums.TransactionType;
import com.banking.core.exception.AccountNotFoundException;
import com.banking.core.exception.BankingException;
import com.banking.core.exception.InsufficientFundsException;
import com.banking.core.exception.TransactionFailedException;
import com.banking.core.repository.AccountRepository;
import com.banking.core.repository.BranchRepository;
import com.banking.core.repository.TransactionRepository;
import com.banking.core.service.AccountService;
import com.banking.core.util.AccountNumberGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of AccountService
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final BranchRepository branchRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                             BranchRepository branchRepository,
                             TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.branchRepository = branchRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Account createAccount(User user, AccountType accountType, String branchIfsc) {
        return createAccount(user, accountType, branchIfsc, BigDecimal.ZERO);
    }

    @Override
    public Account createAccount(User user, AccountType accountType, String branchIfsc, BigDecimal initialDeposit) {
        logger.info("Creating account for user: {} with type: {}", user.getEmail(), accountType);
        
        // Find branch
        Branch branch = branchRepository.findByIfsc(branchIfsc)
                .orElseThrow(() -> new BankingException("Branch not found with IFSC: " + branchIfsc, "BRANCH_NOT_FOUND"));
        
        // Generate unique account number
        String accountNumber;
        do {
            accountNumber = AccountNumberGenerator.generateAccountNumber(accountType);
        } while (accountRepository.existsByAccountNumber(accountNumber));
        
        // Create account
        Account account = new Account(accountNumber, accountType, user, branch);
        account.setBalance(initialDeposit);
        
        // Set interest rate based on account type
        switch (accountType) {
            case SAVINGS:
                account.setInterestRate(BigDecimal.valueOf(3.5));
                break;
            case CURRENT:
                account.setInterestRate(BigDecimal.ZERO);
                account.setOverdraftLimit(BigDecimal.valueOf(10000)); // Default overdraft
                break;
            case SALARY:
                account.setInterestRate(BigDecimal.valueOf(2.5));
                break;
            case FIXED_DEPOSIT:
                account.setInterestRate(BigDecimal.valueOf(6.5));
                break;
        }
        
        Account savedAccount = accountRepository.save(account);
        
        // Record initial deposit if any
        if (initialDeposit.compareTo(BigDecimal.ZERO) > 0) {
            recordTransaction(TransactionType.DEPOSIT, initialDeposit, null, savedAccount, 
                    "Initial deposit", savedAccount.getBalance());
        }
        
        logger.info("Account created successfully: {}", savedAccount.getAccountNumber());
        return savedAccount;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Account> findById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> findByUser(User user) {
        return accountRepository.findByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Account> findByUser(User user, Pageable pageable) {
        return accountRepository.findByUser(user, pageable);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Account deposit(String accountNumber, BigDecimal amount, String narration) {
        logger.info("Deposit request - Account: {}, Amount: {}", accountNumber, amount);
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankingException("Deposit amount must be positive", "INVALID_AMOUNT");
        }
        
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        
        if (account.getStatus() != Status.ACTIVE) {
            throw new BankingException("Account is not active", "ACCOUNT_INACTIVE");
        }
        
        account.credit(amount);
        Account savedAccount = accountRepository.save(account);
        
        // Record transaction
        recordTransaction(TransactionType.DEPOSIT, amount, null, savedAccount, 
                narration, savedAccount.getBalance());
        
        logger.info("Deposit successful - Account: {}, New Balance: {}", accountNumber, savedAccount.getBalance());
        return savedAccount;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Account withdraw(String accountNumber, BigDecimal amount, String narration) {
        logger.info("Withdrawal request - Account: {}, Amount: {}", accountNumber, amount);
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankingException("Withdrawal amount must be positive", "INVALID_AMOUNT");
        }
        
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        
        if (account.getStatus() != Status.ACTIVE) {
            throw new BankingException("Account is not active", "ACCOUNT_INACTIVE");
        }
        
        if (!account.canWithdraw(amount)) {
            throw new InsufficientFundsException(account.getAvailableBalance(), amount);
        }
        
        account.debit(amount);
        Account savedAccount = accountRepository.save(account);
        
        // Record transaction
        recordTransaction(TransactionType.WITHDRAWAL, amount, savedAccount, null, 
                narration, savedAccount.getBalance());
        
        logger.info("Withdrawal successful - Account: {}, New Balance: {}", accountNumber, savedAccount.getBalance());
        return savedAccount;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String narration) {
        logger.info("Transfer request - From: {}, To: {}, Amount: {}", fromAccountNumber, toAccountNumber, amount);
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankingException("Transfer amount must be positive", "INVALID_AMOUNT");
        }
        
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new BankingException("Cannot transfer to the same account", "INVALID_TRANSFER");
        }
        
        try {
            Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                    .orElseThrow(() -> new AccountNotFoundException(fromAccountNumber));
            
            Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                    .orElseThrow(() -> new AccountNotFoundException(toAccountNumber));
            
            // Validate accounts
            if (fromAccount.getStatus() != Status.ACTIVE) {
                throw new BankingException("Source account is not active", "ACCOUNT_INACTIVE");
            }
            
            if (toAccount.getStatus() != Status.ACTIVE) {
                throw new BankingException("Destination account is not active", "ACCOUNT_INACTIVE");
            }
            
            if (!fromAccount.canWithdraw(amount)) {
                throw new InsufficientFundsException(fromAccount.getAvailableBalance(), amount);
            }
            
            // Perform transfer
            fromAccount.debit(amount);
            toAccount.credit(amount);
            
            // Save both accounts
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            
            // Record transaction
            recordTransaction(TransactionType.TRANSFER, amount, fromAccount, toAccount, 
                    narration, fromAccount.getBalance());
            
            logger.info("Transfer successful - From: {} (Balance: {}), To: {} (Balance: {})", 
                    fromAccountNumber, fromAccount.getBalance(), toAccountNumber, toAccount.getBalance());
            
        } catch (Exception e) {
            logger.error("Transfer failed - From: {}, To: {}, Amount: {}", fromAccountNumber, toAccountNumber, amount, e);
            throw new TransactionFailedException("Transfer failed: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        return account.getBalance();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAvailableBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        return account.getAvailableBalance();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canWithdraw(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        return account.canWithdraw(amount);
    }

    @Override
    public void closeAccount(String accountNumber, String reason) {
        logger.info("Closing account: {} - Reason: {}", accountNumber, reason);
        
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new BankingException("Cannot close account with non-zero balance", "ACCOUNT_HAS_BALANCE");
        }
        
        account.setStatus(Status.CLOSED);
        accountRepository.save(account);
        
        logger.info("Account closed successfully: {}", accountNumber);
    }

    @Override
    public void activateAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        
        account.setStatus(Status.ACTIVE);
        accountRepository.save(account);
        
        logger.info("Account activated: {}", accountNumber);
    }

    @Override
    public void blockAccount(String accountNumber, String reason) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        
        account.setStatus(Status.BLOCKED);
        accountRepository.save(account);
        
        logger.info("Account blocked: {} - Reason: {}", accountNumber, reason);
    }

    @Override
    public void unblockAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        
        account.setStatus(Status.ACTIVE);
        accountRepository.save(account);
        
        logger.info("Account unblocked: {}", accountNumber);
    }

    @Override
    public void updateOverdraftLimit(String accountNumber, BigDecimal overdraftLimit) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        
        if (overdraftLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new BankingException("Overdraft limit cannot be negative", "INVALID_OVERDRAFT_LIMIT");
        }
        
        account.setOverdraftLimit(overdraftLimit);
        accountRepository.save(account);
        
        logger.info("Overdraft limit updated for account {}: {}", accountNumber, overdraftLimit);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalBalance(User user) {
        return accountRepository.getTotalBalanceByUser(user, Status.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> findDormantAccounts(int daysSinceLastTransaction) {
        return accountRepository.findDormantAccounts(daysSinceLastTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public Object getAccountSummary(User user) {
        List<Account> accounts = accountRepository.findByUser(user);
        BigDecimal totalBalance = getTotalBalance(user);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalAccounts", accounts.size());
        summary.put("totalBalance", totalBalance);
        summary.put("accounts", accounts);
        
        Map<AccountType, Long> accountsByType = new HashMap<>();
        for (Account account : accounts) {
            accountsByType.merge(account.getAccountType(), 1L, Long::sum);
        }
        summary.put("accountsByType", accountsByType);
        
        return summary;
    }

    /**
     * Helper method to record transactions
     */
    private void recordTransaction(TransactionType type, BigDecimal amount, Account fromAccount, 
                                 Account toAccount, String narration, BigDecimal balanceAfter) {
        try {
            String txnId = AccountNumberGenerator.generateTransactionId();
            
            Transaction transaction = new Transaction(txnId, type, amount, fromAccount, toAccount, narration);
            transaction.setTimestamp(LocalDateTime.now());
            transaction.setBalanceAfter(balanceAfter);
            
            transactionRepository.save(transaction);
            
        } catch (Exception e) {
            logger.error("Failed to record transaction", e);
            // Don't throw exception here as the main transaction should not fail due to logging issues
        }
    }
}
