# Banking System - Complete JAR Distribution

## 🎆 Project Conversion Complete!

Both projects have been successfully converted to JAR distributions and organized in the `dist/` folder.

## 📋 Distribution Summary

### 📁 Folder Structure
```
dist/
├── banking-core/
│   └── banking-core-1.0.0-boot.jar     # 45MB - Standalone Spring Boot app
├── netbanking-app/
│   └── netbanking-app-1.0.0.jar        # 61MB - Complete web application
├── lib/
│   ├── banking-core-1.0.0.jar          # 74KB - Library JAR
│   └── banking-core-1.0.0-sources.jar  # 44KB - Source code
├── scripts/
│   ├── start-banking-core.sh/.bat      # Startup scripts
│   └── start-netbanking-app.sh/.bat    # Cross-platform support
├── config/
│   ├── banking-core-application.properties
│   └── netbanking-app-application.properties
├── docs/
│   ├── README.md
│   ├── curl-examples.md
│   └── Banking-System-API.postman_collection.json
├── README.md                        # Distribution guide
├── DEPLOYMENT_INFO.md               # Detailed deployment info
└── start-all.sh                     # Interactive launcher
```

### 🚀 Quick Start Commands

#### Method 1: Interactive Launcher
```bash
cd dist/
./start-all.sh
```

#### Method 2: Direct Scripts
```bash
# Linux/Mac
cd dist/
./scripts/start-netbanking-app.sh

# Windows
cd dist
scripts\start-netbanking-app.bat
```

#### Method 3: Direct JAR Execution
```bash
cd dist/
java -jar netbanking-app/netbanking-app-1.0.0.jar
```

## 🔍 JAR Details

| JAR File | Size | Type | Purpose |
|----------|------|------|----------|
| `banking-core-1.0.0-boot.jar` | 45MB | Executable | Standalone banking service |
| `banking-core-1.0.0.jar` | 74KB | Library | Reusable banking domain library |
| `netbanking-app-1.0.0.jar` | 61MB | Executable | Complete web banking application |
| `banking-core-1.0.0-sources.jar` | 44KB | Sources | Source code for development |

## 🎯 Application Access Points

### NetBanking Application (Primary)
- **Base URL**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **H2 Console**: http://localhost:8080/api/h2-console
- **Health Check**: http://localhost:8080/api/

### Banking Core (Standalone)
- **Base URL**: http://localhost:8081
- **H2 Console**: http://localhost:8081/h2-console
- **Service**: Backend banking operations

## 🔐 Sample Credentials
```
Admin:
- Email: admin@bank.com
- Password: Admin@123

User:
- Email: user1@bank.com
- Password: User@123
```

## ⚙️ Configuration Features

### Pre-configured Settings
- **Database**: H2 in-memory (development)
- **Security**: JWT authentication with 24h expiration
- **CORS**: Configured for common frontend ports
- **Logging**: Debug level for development
- **Sample Data**: Pre-loaded users, accounts, transactions

### Customizable Options
- Port numbers (8080 for netbanking, 8081 for core)
- Database connections (switch to MySQL/PostgreSQL)
- JWT secret and expiration
- CORS allowed origins
- Log levels and output format

## 🛠 Development & Integration

### Using as Library
The `lib/banking-core-1.0.0.jar` can be integrated into other projects:

```xml
<dependency>
    <groupId>com.banking</groupId>
    <artifactId>banking-core</artifactId>
    <version>1.0.0</version>
    <scope>system</scope>
    <systemPath>${basedir}/lib/banking-core-1.0.0.jar</systemPath>
</dependency>
```

### API Integration
Use the Swagger documentation at http://localhost:8080/api/swagger-ui.html for API integration.

## 📊 System Requirements

### Minimum
- Java 17+
- 2GB RAM
- 200MB disk space

### Recommended
- Java 17+
- 4GB RAM
- 500MB disk space
- SSD storage

## 🌐 Deployment Scenarios

### 1. Development
- Use provided H2 configuration
- Start with scripts or direct JAR execution
- Access applications on localhost

### 2. Testing
- Configure external database
- Use test profile configurations
- Deploy to test servers

### 3. Production
- Update configurations for production database
- Use production-grade JVM settings
- Configure reverse proxy and SSL
- Set up monitoring and logging

## 📚 Documentation Included

- **README.md**: Complete user guide for the dist folder
- **DEPLOYMENT_INFO.md**: Detailed deployment and troubleshooting
- **docs/README.md**: Original project documentation
- **docs/curl-examples.md**: API testing examples
- **docs/Banking-System-API.postman_collection.json**: Postman collection

## ✅ Verification Steps

1. **Extract/Navigate to dist folder**
2. **Run**: `./scripts/start-netbanking-app.sh` (or .bat on Windows)
3. **Wait**: ~30-60 seconds for startup
4. **Access**: http://localhost:8080/api/swagger-ui.html
5. **Login**: Use admin@bank.com / Admin@123
6. **Test**: Try the API endpoints

## 🆙 Troubleshooting

If you encounter issues:
1. Check Java version: `java -version`
2. Verify ports are available: `netstat -tulpn | grep :8080`
3. Check logs in console output
4. Review configuration files in `config/`
5. Consult `DEPLOYMENT_INFO.md` for detailed troubleshooting

## 🎉 Success!

Your Banking System is now fully packaged and ready for deployment! 

The `dist/` folder contains everything needed to run both applications independently or together, with comprehensive documentation and cross-platform support.
