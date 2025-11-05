package com.banking.core.config;

import com.banking.core.entity.*;
import com.banking.core.enums.*;
import com.banking.core.repository.*;
import com.banking.core.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Data initializer to create sample data for development and testing
 */
@Component
@Profile({"!test"}) // Don't run in test profile
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final AccountRepository accountRepository;
    private final BankPolicyRepository bankPolicyRepository;
    private final CreditCardRepository creditCardRepository;
    private final LoanRepository loanRepository;
    private final InsuranceApplicationRepository insuranceRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository,
                          BranchRepository branchRepository,
                          AccountRepository accountRepository,
                          BankPolicyRepository bankPolicyRepository,
                          CreditCardRepository creditCardRepository,
                          LoanRepository loanRepository,
                          InsuranceApplicationRepository insuranceRepository) {
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
        this.accountRepository = accountRepository;
        this.bankPolicyRepository = bankPolicyRepository;
        this.creditCardRepository = creditCardRepository;
        this.loanRepository = loanRepository;
        this.insuranceRepository = insuranceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Initializing sample data...");
        
        // Initialize in order due to dependencies
        initializeBranches();
        initializeUsers();
        initializeBankPolicies();
        initializeAccountsAndTransactions();
        initializeCreditCards();
        initializeLoans();
        initializeInsurance();
        
        logger.info("Sample data initialization completed");
    }

    private void initializeBranches() {
        if (branchRepository.count() > 0) {
            logger.info("Branches already exist, skipping initialization");
            return;
        }
        
        logger.info("Creating sample branches...");
        
        // Main Branch
        Address mainAddress = new Address("123 Main Street", "New York", "NY", "10001", "USA");
        Branch mainBranch = new Branch("Main Branch", "BANK0001234", mainAddress);
        mainBranch.setContact("+1-555-0100");
        mainBranch.setEmail("main@bank.com");
        mainBranch.setManagerName("John Smith");
        mainBranch.setLatitude(BigDecimal.valueOf(40.7128));
        mainBranch.setLongitude(BigDecimal.valueOf(-74.0060));
        branchRepository.save(mainBranch);
        
        // Downtown Branch
        Address downtownAddress = new Address("456 Downtown Ave", "New York", "NY", "10002", "USA");
        Branch downtownBranch = new Branch("Downtown Branch", "BANK0005678", downtownAddress);
        downtownBranch.setContact("+1-555-0200");
        downtownBranch.setEmail("downtown@bank.com");
        downtownBranch.setManagerName("Jane Doe");
        downtownBranch.setLatitude(BigDecimal.valueOf(40.7489));
        downtownBranch.setLongitude(BigDecimal.valueOf(-73.9857));
        branchRepository.save(downtownBranch);
        
        logger.info("Created {} branches", branchRepository.count());
    }

    private void initializeUsers() {
        if (userRepository.count() > 0) {
            logger.info("Users already exist, skipping initialization");
            return;
        }
        
        logger.info("Creating sample users...");
        
        // Admin User
        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail("admin@bank.com");
        admin.setMobile("+1-555-0001");
        admin.setDateOfBirth(LocalDate.of(1980, 1, 1));
        admin.setPasswordHash(PasswordUtil.encode("Admin@123"));
        admin.setRole("ROLE_ADMIN");
        admin.setStatus(Status.ACTIVE);
        
        Address adminAddress = new Address("789 Admin Street", "New York", "NY", "10003", "USA");
        admin.setAddress(adminAddress);
        
        userRepository.save(admin);
        
        // Regular User 1
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Customer");
        user1.setEmail("user1@bank.com");
        user1.setMobile("+1-555-0002");
        user1.setDateOfBirth(LocalDate.of(1990, 5, 15));
        user1.setPasswordHash(PasswordUtil.encode("User@123"));
        user1.setRole("ROLE_USER");
        user1.setStatus(Status.ACTIVE);
        
        Address user1Address = new Address("321 User Lane", "New York", "NY", "10004", "USA");
        user1.setAddress(user1Address);
        
        userRepository.save(user1);
        
        // Regular User 2
        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("user2@bank.com");
        user2.setMobile("+1-555-0003");
        user2.setDateOfBirth(LocalDate.of(1985, 8, 22));
        user2.setPasswordHash(PasswordUtil.encode("User@123"));
        user2.setRole("ROLE_USER");
        user2.setStatus(Status.ACTIVE);
        
        Address user2Address = new Address("654 Customer Blvd", "New York", "NY", "10005", "USA");
        user2.setAddress(user2Address);
        
        userRepository.save(user2);
        
        logger.info("Created {} users", userRepository.count());
    }

    private void initializeBankPolicies() {
        if (bankPolicyRepository.count() > 0) {
            logger.info("Bank policies already exist, skipping initialization");
            return;
        }
        
        logger.info("Creating sample bank policies...");
        
        // Interest Rate Policy
        BankPolicy interestPolicy = new BankPolicy();
        interestPolicy.setPolicyCode("INT001");
        interestPolicy.setTitle("Interest Rate Policy");
        interestPolicy.setDescription("Current interest rates for savings accounts: 3.5% per annum, Current accounts: 0% per annum");
        interestPolicy.setEffectiveDate(LocalDate.now().minusMonths(1));
        interestPolicy.setCategory("Interest Rates");
        interestPolicy.setPolicyVersion("1.0");
        interestPolicy.setCreatedBy("System");
        interestPolicy.setApprovedBy("Admin");
        bankPolicyRepository.save(interestPolicy);
        
        // Loan Policy
        BankPolicy loanPolicy = new BankPolicy();
        loanPolicy.setPolicyCode("LOAN001");
        loanPolicy.setTitle("Personal Loan Policy");
        loanPolicy.setDescription("Personal loans available from $1,000 to $100,000 with competitive interest rates starting from 8.5%");
        loanPolicy.setEffectiveDate(LocalDate.now().minusMonths(2));
        loanPolicy.setCategory("Loans");
        loanPolicy.setPolicyVersion("2.1");
        loanPolicy.setCreatedBy("System");
        loanPolicy.setApprovedBy("Admin");
        bankPolicyRepository.save(loanPolicy);
        
        logger.info("Created {} bank policies", bankPolicyRepository.count());
    }

    private void initializeAccountsAndTransactions() {
        if (accountRepository.count() > 0) {
            logger.info("Accounts already exist, skipping initialization");
            return;
        }
        
        logger.info("Creating sample accounts...");
        
        Optional<User> user1Opt = userRepository.findByEmail("user1@bank.com");
        Optional<User> user2Opt = userRepository.findByEmail("user2@bank.com");
        Optional<Branch> mainBranchOpt = branchRepository.findByIfsc("BANK0001234");
        Optional<Branch> downtownBranchOpt = branchRepository.findByIfsc("BANK0005678");
        
        if (user1Opt.isEmpty() || user2Opt.isEmpty() || mainBranchOpt.isEmpty() || downtownBranchOpt.isEmpty()) {
            logger.error("Required entities not found for account creation");
            return;
        }
        
        User user1 = user1Opt.get();
        User user2 = user2Opt.get();
        Branch mainBranch = mainBranchOpt.get();
        Branch downtownBranch = downtownBranchOpt.get();
        
        // User1 Savings Account
        Account user1Savings = new Account("123401000000001", AccountType.SAVINGS, user1, mainBranch);
        user1Savings.setBalance(BigDecimal.valueOf(5000.00));
        user1Savings.setInterestRate(BigDecimal.valueOf(3.5));
        accountRepository.save(user1Savings);
        
        // User1 Current Account
        Account user1Current = new Account("123402000000001", AccountType.CURRENT, user1, mainBranch);
        user1Current.setBalance(BigDecimal.valueOf(2500.00));
        user1Current.setOverdraftLimit(BigDecimal.valueOf(10000.00));
        accountRepository.save(user1Current);
        
        // User2 Savings Account
        Account user2Savings = new Account("567801000000001", AccountType.SAVINGS, user2, downtownBranch);
        user2Savings.setBalance(BigDecimal.valueOf(7500.00));
        user2Savings.setInterestRate(BigDecimal.valueOf(3.5));
        accountRepository.save(user2Savings);
        
        logger.info("Created {} accounts", accountRepository.count());
    }

    private void initializeCreditCards() {
        logger.info("Creating sample credit cards...");
        
        Optional<User> user1Opt = userRepository.findByEmail("user1@bank.com");
        if (user1Opt.isEmpty()) {
            return;
        }
        
        User user1 = user1Opt.get();
        
        // User1 Credit Card
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber("4111111111111111");
        creditCard.setCardType(CardType.VISA);
        creditCard.setExpirationDate(LocalDate.now().plusYears(3));
        creditCard.setCvvHash(PasswordUtil.encode("123"));
        creditCard.setCreditLimit(BigDecimal.valueOf(50000.00));
        creditCard.setAvailableCredit(BigDecimal.valueOf(48500.00));
        creditCard.setOutstandingBalance(BigDecimal.valueOf(1500.00));
        creditCard.setInterestRate(BigDecimal.valueOf(18.0));
        creditCard.setMinimumPayment(BigDecimal.valueOf(75.00));
        creditCard.setPaymentDueDate(LocalDate.now().plusDays(15));
        creditCard.setActivationDate(LocalDate.now().minusMonths(6));
        creditCard.setUser(user1);
        
        creditCardRepository.save(creditCard);
        
        logger.info("Created {} credit cards", creditCardRepository.count());
    }

    private void initializeLoans() {
        logger.info("Creating sample loans...");
        
        Optional<User> user1Opt = userRepository.findByEmail("user1@bank.com");
        if (user1Opt.isEmpty()) {
            return;
        }
        
        User user1 = user1Opt.get();
        
        // User1 Personal Loan
        Loan loan = new Loan();
        loan.setLoanNumber("LN2401000000001");
        loan.setLoanType(LoanType.PERSONAL);
        loan.setPrincipal(BigDecimal.valueOf(25000.00));
        loan.setInterestRate(BigDecimal.valueOf(9.5));
        loan.setTermMonths(36);
        loan.setOutstandingAmount(BigDecimal.valueOf(18500.00));
        loan.setEmiAmount(BigDecimal.valueOf(800.00));
        loan.setStartDate(LocalDate.now().minusMonths(8));
        loan.setEndDate(LocalDate.now().plusMonths(28));
        loan.setNextPaymentDate(LocalDate.now().plusDays(22));
        loan.setTotalPaymentsMade(8);
        loan.setStatus(Status.ACTIVE);
        loan.setPurpose("Home renovation");
        loan.setApprovedBy("Admin User");
        loan.setApprovalDate(LocalDate.now().minusMonths(9));
        loan.setDisbursedDate(LocalDate.now().minusMonths(8));
        loan.setDisbursedAmount(BigDecimal.valueOf(25000.00));
        loan.setUser(user1);
        
        loanRepository.save(loan);
        
        logger.info("Created {} loans", loanRepository.count());
    }

    private void initializeInsurance() {
        logger.info("Creating sample insurance applications...");
        
        Optional<User> user2Opt = userRepository.findByEmail("user2@bank.com");
        if (user2Opt.isEmpty()) {
            return;
        }
        
        User user2 = user2Opt.get();
        
        // User2 Life Insurance
        InsuranceApplication insurance = new InsuranceApplication();
        insurance.setPolicyNumber("POL2401000000001");
        insurance.setPolicyType("Life Insurance");
        insurance.setPremiumAmount(BigDecimal.valueOf(2400.00));
        insurance.setCoverageAmount(BigDecimal.valueOf(500000.00));
        insurance.setTermYears(20);
        insurance.setApplicationDate(LocalDate.now().minusMonths(3));
        insurance.setStartDate(LocalDate.now().minusMonths(2));
        insurance.setEndDate(LocalDate.now().plusYears(19).plusMonths(10));
        insurance.setStatus(Status.APPROVED);
        insurance.setBeneficiaryName("John Smith");
        insurance.setBeneficiaryRelation("Spouse");
        insurance.setApprovedBy("Admin User");
        insurance.setApprovalDate(LocalDate.now().minusMonths(2).minusDays(15));
        insurance.setNextPremiumDate(LocalDate.now().plusMonths(1));
        insurance.setPaymentFrequency("MONTHLY");
        insurance.setUser(user2);
        
        insuranceRepository.save(insurance);
        
        logger.info("Created {} insurance applications", insuranceRepository.count());
    }
}
