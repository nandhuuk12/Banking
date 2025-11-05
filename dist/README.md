# Banking System Distribution

This folder contains the compiled JAR distributions for both Banking Core and NetBanking Application projects.

## 📁 Directory Structure

```
dist/
├── banking-core/               # Banking Core standalone application
│   ├── banking-core-1.0.0-boot.jar (45MB)
│   └── banking-core-1.0.0.jar (74KB)
├── netbanking-app/             # NetBanking web application
│   ├── netbanking-app-1.0.0.jar (61MB)
│   └── lib/                    # Embedded dependencies
│       ├── banking-core-1.0.0.jar
│       └── banking-core-1.0.0-sources.jar
├── lib/                        # Shared library JARs
│   ├── banking-core-1.0.0.jar     # Core library (74KB)
│   └── banking-core-1.0.0-sources.jar # Source code
├── scripts/                    # Startup scripts
│   ├── start-banking-core.sh      # Unix/Linux/Mac
│   ├── start-banking-core.bat     # Windows
│   ├── start-netbanking-app.sh    # Unix/Linux/Mac
│   └── start-netbanking-app.bat   # Windows
├── config/                     # Configuration files
│   ├── banking-core-application.properties
│   └── netbanking-app-application.properties
├── docs/                       # Documentation
└── README.md                   # This file
```

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- At least 2GB RAM available

### Option 1: Using Startup Scripts (Recommended)

#### Linux/Mac:
```bash
# Start Banking Core (standalone)
./scripts/start-banking-core.sh

# Start NetBanking Application
./scripts/start-netbanking-app.sh
```

#### Windows:
```batch
REM Start Banking Core (standalone)
scripts\start-banking-core.bat

REM Start NetBanking Application
scripts\start-netbanking-app.bat
```

### Option 2: Direct JAR Execution

#### Banking Core (Standalone Service)
```bash
java -jar banking-core/banking-core-1.0.0-boot.jar
```
Access at: http://localhost:8081

#### NetBanking Application
```bash
java -jar netbanking-app/netbanking-app-1.0.0.jar
```
Access at: http://localhost:8080/api

## 🔧 Configuration

### Banking Core Configuration
- Default port: 8081
- H2 Console: http://localhost:8081/h2-console
- Database: `jdbc:h2:mem:banking_core_db`
- Config file: `config/banking-core-application.properties`

### NetBanking Application Configuration
- Default port: 8080
- Context path: `/api`
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- H2 Console: http://localhost:8080/api/h2-console
- Database: `jdbc:h2:mem:netbanking_db`
- Config file: `config/netbanking-app-application.properties`

## 🎯 Applications Overview

### Banking Core (banking-core-1.0.0-boot.jar)
- **Purpose**: Standalone banking domain service
- **Size**: 45MB (includes all dependencies)
- **Features**:
  - Complete banking domain model
  - JPA repositories and services
  - Sample data initialization
  - H2 in-memory database
  - Can run independently or be used as a library

### NetBanking Application (netbanking-app-1.0.0.jar)
- **Purpose**: Complete web banking application
- **Size**: 61MB (includes banking-core + web layer)
- **Features**:
  - JWT-based authentication
  - REST API endpoints
  - Swagger documentation
  - CORS support
  - Security configuration
  - Uses banking-core as dependency

## 📚 API Documentation

Once NetBanking Application is running:
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **API Docs**: http://localhost:8080/api/v3/api-docs

## 🔐 Sample Login Credentials

```
Admin User:
- Email: admin@bank.com
- Password: Admin@123
- Role: ADMIN

Regular User:
- Email: user1@bank.com
- Password: User@123
- Role: USER
```

## 🛠 Development

### Using as Library
The `lib/banking-core-1.0.0.jar` can be used as a dependency in other Maven/Gradle projects:

```xml
<dependency>
    <groupId>com.banking</groupId>
    <artifactId>banking-core</artifactId>
    <version>1.0.0</version>
    <scope>system</scope>
    <systemPath>${basedir}/lib/banking-core-1.0.0.jar</systemPath>
</dependency>
```

### Environment Profiles
- `dev`: Development mode (H2 database, debug logging)
- `prod`: Production mode (MySQL database, optimized settings)
- `test`: Test mode (H2 database, test data)

## 🐛 Troubleshooting

### Common Issues

1. **Port already in use**
   ```bash
   # Change port in config file or use command line
   java -jar netbanking-app-1.0.0.jar --server.port=9090
   ```

2. **Out of memory**
   ```bash
   # Increase heap size
   java -Xmx2g -jar netbanking-app-1.0.0.jar
   ```

3. **Database connection issues**
   - Check H2 console at `/h2-console`
   - Verify JDBC URL in properties file

### Log Files
Applications log to console by default. To save logs:
```bash
java -jar netbanking-app-1.0.0.jar > app.log 2>&1
```

## 📦 Production Deployment

For production deployment:
1. Update configuration files with production database settings
2. Use external configuration: `--spring.config.location=/path/to/config/`
3. Set appropriate JVM options: `-Xmx2g -XX:+UseG1GC`
4. Consider using systemd service or Docker containers

## 🔄 Updates

To update the applications:
1. Replace JAR files in respective directories
2. Update configuration if needed
3. Restart applications

## 📞 Support

For issues and questions:
- Check the `docs/` folder for detailed documentation
- Review API documentation via Swagger UI
- Check application logs for error details
