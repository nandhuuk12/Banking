# NetBanking Application

Complete web-based banking application with REST APIs and JWT authentication.

## 📦 Contents

### `netbanking-app-1.0.0.jar` (61MB)
- **Type**: Executable Spring Boot JAR (Fat JAR)
- **Purpose**: Complete web banking application
- **Contains**: NetBanking app + Banking-Core + All dependencies
- **Usage**: `java -jar netbanking-app-1.0.0.jar`

### `lib/` Folder
- `banking-core-1.0.0.jar` - Core banking domain library
- `banking-core-1.0.0-sources.jar` - Source code
- `README.md` - Detailed dependency information

## 🚀 Quick Start

### Option 1: Direct JAR Execution
```bash
java -jar netbanking-app-1.0.0.jar
```

### Option 2: Using Startup Scripts
```bash
# From dist folder
../scripts/start-netbanking-app.sh

# Or on Windows
..\scripts\start-netbanking-app.bat
```

### Option 3: With Custom Configuration
```bash
java -jar netbanking-app-1.0.0.jar \
  --spring.config.location=../config/netbanking-app-application.properties
```

## 🎯 Access Points

Once started, the application will be available at:

- **Main API**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **H2 Console**: http://localhost:8080/api/h2-console
- **Health Check**: http://localhost:8080/api/actuator/health (if enabled)

## 🔐 Authentication

### Sample Users
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

### JWT Authentication
1. **Login**: POST `/api/auth/login` with credentials
2. **Get Token**: Response includes JWT token
3. **Use Token**: Include in `Authorization: Bearer <token>` header
4. **Token Expiry**: 24 hours (configurable)

## 📚 API Documentation

### Available Endpoints

#### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/refresh` - Refresh JWT token
- `POST /api/auth/logout` - User logout

#### Account Management
- `GET /api/accounts` - List user accounts
- `GET /api/accounts/{id}` - Get account details
- `POST /api/accounts` - Create new account
- `PUT /api/accounts/{id}` - Update account

#### Transactions
- `GET /api/accounts/{id}/transactions` - Account transactions
- `POST /api/accounts/{id}/transactions` - Create transaction
- `GET /api/accounts/{id}/balance` - Get account balance

### Interactive Documentation
Visit http://localhost:8080/api/swagger-ui.html for complete API documentation with testing interface.

## 🔧 Configuration

### Default Settings
- **Port**: 8080
- **Context Path**: /api
- **Database**: H2 in-memory (`jdbc:h2:mem:netbanking_db`)
- **JWT Secret**: `mySecretKey` (change in production)
- **JWT Expiry**: 24 hours
- **CORS**: Enabled for localhost:3000, localhost:4200

### Environment Profiles
- **dev**: Development mode (default)
- **test**: Testing mode
- **prod**: Production mode

```bash
# Use specific profile
java -jar netbanking-app-1.0.0.jar --spring.profiles.active=prod
```

### Custom Configuration
Use the configuration file at `../config/netbanking-app-application.properties` or override via command line:

```bash
java -jar netbanking-app-1.0.0.jar \
  --server.port=9090 \
  --app.jwt.secret=myCustomSecret \
  --spring.datasource.url=jdbc:mysql://localhost:3306/netbanking
```

## 🏗️ Architecture

### Application Layers
```
┌──────────────────────────────┐
│        NetBanking App          │
│  ┌─────────────────────────┐  │
│  │   REST Controllers     │  │
│  │   (Auth, Account)       │  │
│  └─────────────────────────┘  │
│  ┌─────────────────────────┐  │
│  │   Security & JWT       │  │
│  └─────────────────────────┘  │
│  ┌─────────────────────────┐  │
│  │    Banking Core JAR     │  │
│  │  (Domain & Services)   │  │
│  └─────────────────────────┘  │
└──────────────────────────────┘
```

### Key Features
- **JWT Security**: Stateless authentication
- **REST APIs**: RESTful web services
- **Swagger**: Interactive API documentation
- **CORS**: Cross-origin resource sharing
- **JPA**: Database persistence
- **H2 Console**: Database management interface
- **Banking Domain**: Complete banking functionality

## 📊 System Requirements

### Minimum
- Java 17 or higher
- 2GB RAM
- 100MB disk space

### Recommended
- Java 17 or higher
- 4GB RAM
- 500MB disk space
- SSD storage for better performance

## 🐛 Troubleshooting

### Common Issues

**1. Port 8080 already in use**
```bash
# Use different port
java -jar netbanking-app-1.0.0.jar --server.port=8090

# Find what's using port 8080
netstat -tulpn | grep :8080
```

**2. Out of memory**
```bash
# Increase heap size
java -Xmx2g -jar netbanking-app-1.0.0.jar
```

**3. Database connection issues**
- Check H2 console: http://localhost:8080/api/h2-console
- JDBC URL: `jdbc:h2:mem:netbanking_db`
- Username: `sa`, Password: (empty)

**4. Authentication issues**
- Verify user credentials in H2 console
- Check JWT token expiry (24 hours default)
- Ensure Authorization header format: `Bearer <token>`

**5. CORS issues**
- Configure allowed origins in properties
- Check browser developer console for CORS errors

### Debugging

**Enable debug logging:**
```bash
java -jar netbanking-app-1.0.0.jar \
  --logging.level.com.netbanking=DEBUG \
  --logging.level.com.banking=DEBUG
```

**Save logs to file:**
```bash
java -jar netbanking-app-1.0.0.jar > application.log 2>&1 &
tail -f application.log
```

## 📦 Production Deployment

### Production Checklist
- [ ] Update JWT secret key
- [ ] Configure production database (MySQL/PostgreSQL)
- [ ] Set appropriate CORS origins
- [ ] Configure SSL/HTTPS
- [ ] Set up reverse proxy (nginx/Apache)
- [ ] Configure logging to files
- [ ] Set up monitoring and health checks
- [ ] Configure backup strategy

### Production Configuration
```bash
java -Xmx2g -XX:+UseG1GC \
  -jar netbanking-app-1.0.0.jar \
  --spring.profiles.active=prod \
  --spring.config.location=/etc/netbanking/application.properties
```

## 🔄 Updates

### Application Updates
1. Stop current application
2. Backup configuration files
3. Replace JAR file with new version
4. Start application with same configuration
5. Verify functionality

### Database Migration
- For production: Use proper database migration tools
- For development: H2 recreates schema automatically

## 📞 Support

### Logs Location
- Console output (default)
- Configure file logging in properties

### Health Monitoring
- Application startup logs
- JWT authentication logs
- Database connection logs
- API request/response logs (if enabled)

### Performance Tuning
```bash
# Production JVM settings
java -Xmx2g -Xms1g \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+PrintGCDetails \
  -jar netbanking-app-1.0.0.jar
```
