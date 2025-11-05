# NetBanking App - Library Dependencies

This folder contains the JAR dependencies for the NetBanking Application.

## 📦 JAR Files

### `banking-core-1.0.0.jar` (74KB)
- **Purpose**: Core banking domain library
- **Type**: Library JAR (dependency)
- **Contains**: Banking entities, services, repositories, utilities
- **Usage**: Embedded in NetBanking application

### `banking-core-1.0.0-sources.jar` (44KB)
- **Purpose**: Source code for banking-core
- **Type**: Sources JAR
- **Contains**: Java source files
- **Usage**: Development and debugging

## 🔗 Integration Details

### Maven Configuration
The NetBanking application uses these JARs via Maven system dependency:

```xml
<dependency>
    <groupId>com.banking</groupId>
    <artifactId>banking-core</artifactId>
    <version>1.0.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/lib/banking-core-1.0.0.jar</systemPath>
</dependency>
```

### What's Included from Banking Core

#### Domain Entities
- `User` - User management and authentication
- `Account` - Bank accounts (savings, current, etc.)
- `Transaction` - Financial transactions
- `CreditCard` - Credit card management
- `Loan` - Loan applications and management
- `InsuranceApplication` - Insurance products
- `Branch` - Bank branch information
- `BankPolicy` - Banking policies and rules

#### Services
- `UserService` - User operations and management
- `AccountService` - Account operations and transactions
- Banking business logic and validation
- Sample data initialization

#### Repositories
- JPA repositories for all entities
- Custom queries for complex operations
- Transaction management

#### Utilities
- `PasswordUtil` - Password hashing and validation
- `AccountNumberGenerator` - Account number generation
- `LoanCalculator` - Loan calculation utilities

## 🏗️ Build Process

### How JARs are Included
1. Banking-core is built first: `mvn clean package`
2. JAR is copied to `lib/` folder
3. NetBanking app includes it as system dependency
4. Final JAR contains both applications

### Verification
```bash
# Check JAR contents
jar -tf banking-core-1.0.0.jar | head -10

# Verify classes are accessible
jar -tf ../netbanking-app-1.0.0.jar | grep "com/banking/core"
```

## 🔄 Updates

### When to Update
- Banking-core functionality changes
- New entities or services added
- Bug fixes in core banking logic

### Update Process
1. Rebuild banking-core: `cd ../../banking-core && mvn clean package`
2. Copy new JAR: `cp target/banking-core-1.0.0.jar ../netbanking-app/lib/`
3. Rebuild NetBanking: `cd ../netbanking-app && mvn clean package`
4. Test integration

## 📊 JAR Analysis

### Banking Core Classes
```bash
# View package structure
jar -tf banking-core-1.0.0.jar | grep -E "\.(class)$" | head -10
```

### Size Breakdown
- **Entities**: ~15KB (JPA entity classes)
- **Services**: ~20KB (Business logic)
- **Repositories**: ~10KB (Data access layer)
- **Utilities**: ~5KB (Helper classes)
- **Configuration**: ~5KB (Spring configuration)
- **Other**: ~19KB (Resources, metadata)

## 🎯 Integration Benefits

### Separation of Concerns
- **Banking-Core**: Domain logic and data management
- **NetBanking-App**: Web layer, security, API endpoints

### Reusability
- Same banking-core can be used in multiple applications
- Consistent business logic across different frontends
- Easy to create additional services (mobile API, admin panel)

### Maintainability
- Core banking logic centralized
- Independent testing of domain layer
- Clean architecture principles

## 🛠️ Development

### IDE Setup
For development with source JARs:
1. Attach `banking-core-1.0.0-sources.jar` in your IDE
2. Enable source lookup for debugging
3. Navigate to banking-core classes easily

### Debugging
- Source code available for step-through debugging
- Full stack traces with line numbers
- IntelliJ/Eclipse can automatically detect sources

## ⚠️ Important Notes

- **Don't modify**: These are compiled JARs, modify source code instead
- **Version consistency**: Ensure JAR version matches expectations
- **Dependencies**: Banking-core JAR doesn't include its dependencies
- **Classpath**: Spring Boot handles classpath automatically
