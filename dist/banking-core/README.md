# Banking Core JAR Files

This folder contains the Banking Core application JAR files.

## 📦 JAR Files Overview

### `banking-core-1.0.0-boot.jar` (45MB)
- **Type**: Executable Spring Boot JAR (Fat JAR)
- **Purpose**: Standalone banking core service
- **Contains**: All dependencies bundled
- **Usage**: Run directly with `java -jar`

### `banking-core-1.0.0.jar` (74KB)
- **Type**: Library JAR (Thin JAR)
- **Purpose**: Banking domain library for integration
- **Contains**: Only banking-core classes
- **Usage**: Include as dependency in other projects

## 🚀 Running Banking Core

### Option 1: Standalone Service (Recommended)
```bash
# Run the standalone Spring Boot application
java -jar banking-core-1.0.0-boot.jar

# With custom port
java -jar banking-core-1.0.0-boot.jar --server.port=8081

# With custom profile
java -jar banking-core-1.0.0-boot.jar --spring.profiles.active=prod
```

### Option 2: Using Startup Scripts
```bash
# From dist folder
../scripts/start-banking-core.sh

# Or on Windows
..\scripts\start-banking-core.bat
```

## 🔌 Access Points

Once started, Banking Core will be available at:
- **Base URL**: http://localhost:8081
- **H2 Console**: http://localhost:8081/h2-console
- **Database**: `jdbc:h2:mem:banking_core_db`
- **Username**: `sa` (no password)

## 📚 Using as Library

To use `banking-core-1.0.0.jar` as a dependency in other Maven projects:

```xml
<dependency>
    <groupId>com.banking</groupId>
    <artifactId>banking-core</artifactId>
    <version>1.0.0</version>
    <scope>system</scope>
    <systemPath>${basedir}/lib/banking-core-1.0.0.jar</systemPath>
</dependency>
```

## 🎯 What's Included

### Domain Models
- User, Account, Transaction
- CreditCard, Loan, InsuranceApplication
- Branch, BankPolicy

### Services
- UserService, AccountService
- Transaction management
- Password utilities

### Features
- JPA repositories with custom queries
- Sample data initialization
- Transaction safety with optimistic locking
- BCrypt password hashing

## ⚙️ Configuration

### Default Settings
- **Port**: 8081
- **Database**: H2 in-memory
- **Profile**: Development
- **Sample Data**: Enabled

### Custom Configuration
Use the configuration file at `../config/banking-core-application.properties` or override with command line:

```bash
java -jar banking-core-1.0.0-boot.jar \
  --server.port=9090 \
  --spring.datasource.url=jdbc:mysql://localhost:3306/banking
```

## 🔧 System Requirements

- **Java**: 17 or higher
- **Memory**: 512MB minimum, 1GB recommended
- **Port**: 8081 (default, configurable)

## 📋 Sample Data

The application comes with pre-loaded sample data:
- **Admin**: admin@bank.com / Admin@123
- **User**: user1@bank.com / User@123
- Sample accounts, transactions, and banking entities

## 🐛 Troubleshooting

### Common Issues

**Port already in use:**
```bash
java -jar banking-core-1.0.0-boot.jar --server.port=8082
```

**Out of memory:**
```bash
java -Xmx1g -jar banking-core-1.0.0-boot.jar
```

**Database issues:**
- Check H2 console at http://localhost:8081/h2-console
- JDBC URL: `jdbc:h2:mem:banking_core_db`
- Username: `sa`, Password: (empty)

### Logs
Application logs are displayed in the console. To save to file:
```bash
java -jar banking-core-1.0.0-boot.jar > banking-core.log 2>&1
```
