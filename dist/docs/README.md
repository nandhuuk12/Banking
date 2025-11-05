# Banking System - Core Library and NetBanking Application

This project consists of two Java Spring Boot applications:

1. **Banking Core** (`banking-core/`) - A standalone library containing all banking domain models, services, and repositories
2. **NetBanking App** (`netbanking-app/`) - A web application that uses the banking-core library to provide REST APIs and web interface for netbanking operations

## Architecture Overview

```
┌─────────────────────┐    depends on     ┌─────────────────────┐
│                     │ ────────────────▶ │                     │
│  NetBanking App     │                   │   Banking Core      │
│                     │                   │                     │
│ - REST Controllers  │                   │ - Domain Entities   │
│ - Security (JWT)    │                   │ - Repositories      │
│ - Web UI            │                   │ - Business Services │
│ - Swagger/OpenAPI   │                   │ - DTOs & Utils      │
│                     │                   │                     │
└─────────────────────┘                   └─────────────────────┘
```

## Technology Stack

- **Java 17** (LTS)
- **Spring Boot 3.2.0**
- **Spring Security** with JWT authentication
- **Spring Data JPA** with Hibernate
- **H2 Database** (development) / **MySQL** (production)
- **Maven** for build management
- **Swagger/OpenAPI** for API documentation
- **JUnit 5** and **Mockito** for testing

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.6+
- MySQL 8.0+ (for production profile)

### Build and Run

1. **Build Banking Core library:**
   ```bash
   cd banking-core
   mvn clean package
   ```

2. **Copy JAR to NetBanking App:**
   ```bash
   cp target/banking-core-1.0.0.jar ../netbanking-app/lib/
   ```

3. **Run NetBanking Application:**
   ```bash
   cd ../netbanking-app
   mvn spring-boot:run
   ```

4. **Access the application:**
   - Web Application: http://localhost:8080/api
   - Swagger UI: http://localhost:8080/api/swagger-ui.html
   - H2 Console: http://localhost:8080/api/h2-console
   - Health Check: http://localhost:8080/api/actuator/health

### Using Automated Build Script

```bash
# Make script executable
chmod +x build-and-run.sh

# Build both projects and run
./build-and-run.sh
```

## Sample Users

The application comes with pre-loaded sample data:

- **Admin User**: admin@bank.com / Admin@123
- **Regular User**: user1@bank.com / User@123

## API Documentation

Once the application is running, visit:
- Interactive API docs: http://localhost:8080/api/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api/v3/api-docs

## Key Features

### Banking Core Features
- User management with secure authentication
- Account management (Savings, Current, Salary, Fixed Deposit)
- Transaction processing with concurrency control
- Credit card management
- Loan processing and EMI calculations
- Insurance policy management
- Bank branch and policy management
- Comprehensive validation and error handling

### NetBanking App Features
- JWT-based authentication and authorization
- RESTful APIs for all banking operations
- Role-based access control (USER, ADMIN)
- Transaction history and statements
- Fund transfers with validation
- Credit card operations
- Loan applications and management
- Insurance policy applications
- Account balance and mini-statements
- Branch locator
- Swagger API documentation

## Project Structure

```
.
├── banking-core/                 # Core banking library
│   ├── src/main/java/com/banking/core/
│   │   ├── entity/               # JPA entities
│   │   ├── repository/           # Data repositories
│   │   ├── service/              # Business services
│   │   ├── dto/                  # Data transfer objects
│   │   ├── enums/                # Enumerations
│   │   ├── exception/            # Custom exceptions
│   │   └── util/                 # Utility classes
│   ├── src/main/resources/
│   │   └── application.yml       # Configuration
│   └── pom.xml
│
├── netbanking-app/               # NetBanking web application
│   ├── lib/                      # External JARs
│   │   └── banking-core-1.0.0.jar
│   ├── src/main/java/com/netbanking/app/
│   │   ├── controller/           # REST controllers
│   │   ├── service/              # Application services
│   │   ├── security/             # Security configuration
│   │   ├── dto/                  # API DTOs
│   │   ├── config/               # Configuration classes
│   │   └── util/                 # Utility classes
│   ├── src/main/resources/
│   │   └── application.yml       # Configuration
│   └── pom.xml
│
├── docker-compose.yml            # Docker compose for MySQL
├── build-and-run.sh             # Build automation script
└── README.md                     # This file
```

## Database Schema

The application uses the following main entities:
- **Users** - Customer information
- **Accounts** - Bank accounts with balances
- **Transactions** - Financial transactions
- **Credit Cards** - Credit card management
- **Loans** - Loan applications and management
- **Insurance Applications** - Insurance policies
- **Branches** - Bank branch information
- **Bank Policies** - Bank policies and terms

## Security

- JWT-based stateless authentication
- Role-based authorization (USER/ADMIN roles)
- Password encryption using BCrypt
- CORS configuration for cross-origin requests
- Input validation and sanitization
- SQL injection prevention through JPA

## Testing

```bash
# Run tests for Banking Core
cd banking-core
mvn test

# Run tests for NetBanking App
cd ../netbanking-app
mvn test
```

## Production Deployment

1. **Update application.yml** for production profile
2. **Set up MySQL database:**
   ```sql
   CREATE DATABASE netbanking_db;
   CREATE USER 'netbanking_user'@'%' IDENTIFIED BY 'netbanking_pass';
   GRANT ALL PRIVILEGES ON netbanking_db.* TO 'netbanking_user'@'%';
   ```

3. **Run with production profile:**
   ```bash
   java -jar netbanking-app/target/netbanking-app-1.0.0.jar --spring.profiles.active=prod
   ```

## Docker Support

```bash
# Start MySQL with Docker Compose
docker-compose up -d mysql

# Build and run application containers
docker-compose up --build
```

## Contributing

1. Follow the existing code style and patterns
2. Add comprehensive tests for new features
3. Update documentation for any API changes
4. Ensure all tests pass before submitting changes

## License

This project is for educational and demonstration purposes.
