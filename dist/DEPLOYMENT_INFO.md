# Banking System - Deployment Information

## 📊 JAR Files Summary

| Application | JAR File | Size | Type | Purpose |
|-------------|----------|------|------|----------|
| Banking Core | `banking-core-1.0.0-boot.jar` | 45MB | Executable | Standalone Spring Boot application |
| Banking Core | `banking-core-1.0.0.jar` | 74KB | Library | Dependency JAR for other projects |
| Banking Core | `banking-core-1.0.0-sources.jar` | 44KB | Sources | Source code for development |
| NetBanking App | `netbanking-app-1.0.0.jar` | 61MB | Executable | Complete web application |

## 🚀 Quick Deployment

### Minimal Requirements
- Java 17+
- 2GB RAM minimum
- 200MB disk space

### Single Command Deployment
```bash
# Make scripts executable
chmod +x scripts/*.sh start-all.sh

# Interactive launcher
./start-all.sh

# Or start directly:
./scripts/start-netbanking-app.sh
```

### Windows Deployment
```batch
REM Start NetBanking Application
scripts\start-netbanking-app.bat
```

## 🔍 Architecture Overview

```
┌─────────────────────────┐
│   NetBanking Application    │
│      (Port 8080)           │
│                            │
│  ┌─────────────────────┐  │
│  │   Banking Core JAR    │  │
│  │   (Embedded Library)  │  │
│  └─────────────────────┘  │
└─────────────────────────┘

      OR (Alternative)

┌─────────────────────────┐
│   Banking Core Service     │
│      (Port 8081)           │
│    Standalone Mode         │
└─────────────────────────┘
```

## 🔧 Configuration Management

### Environment-Specific Configs
1. **Development**: Uses H2 in-memory database
2. **Production**: Switch to MySQL/PostgreSQL in config files

### Custom Configuration
```bash
# Use custom config file
java -jar netbanking-app-1.0.0.jar --spring.config.location=./my-config.properties

# Override specific properties
java -jar netbanking-app-1.0.0.jar --server.port=9090 --spring.datasource.url=jdbc:mysql://localhost:3306/banking
```

## 📊 Performance Tuning

### JVM Options for Production
```bash
java -Xmx2g -Xms1g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 \
     -jar netbanking-app-1.0.0.jar
```

### Resource Usage
- **Memory**: 512MB minimum, 1GB recommended
- **CPU**: Single core minimum, 2+ cores recommended
- **Storage**: Minimal (H2 in-memory), or external database

## 🔒 Security Configuration

### JWT Security (NetBanking App)
- Token expiration: 24 hours
- Secret key: Configurable via `app.jwt.secret`
- CORS: Configurable origins

### Default Security
- BCrypt password hashing
- Role-based authorization (USER, ADMIN)
- Session management disabled (stateless)

## 📊 Monitoring & Health Checks

### Health Endpoints
```bash
# Basic connectivity
curl http://localhost:8080/api/

# H2 Console (development)
http://localhost:8080/api/h2-console
```

### Application Metrics
- View through H2 Console: Database statistics
- Check logs for performance metrics
- Monitor JVM memory usage

## 🐛 Troubleshooting Guide

### Startup Issues
1. **Java Version**: Ensure Java 17+
   ```bash
   java -version
   ```

2. **Port Conflicts**: Change ports in config
   ```bash
   netstat -tulpn | grep :8080
   ```

3. **Memory Issues**: Increase heap size
   ```bash
   java -Xmx2g -jar app.jar
   ```

### Runtime Issues
1. **Database**: Check H2 console
2. **Authentication**: Verify JWT token
3. **CORS**: Configure allowed origins

### Log Analysis
```bash
# Save logs to file
java -jar netbanking-app-1.0.0.jar > application.log 2>&1 &

# Monitor logs
tail -f application.log
```

## 📦 Production Deployment Checklist

- [ ] Update database configuration for production DB
- [ ] Configure JWT secret key
- [ ] Set up SSL/TLS certificates
- [ ] Configure logging (file-based, not console)
- [ ] Set up monitoring and health checks
- [ ] Configure backup strategy for data
- [ ] Set appropriate JVM memory limits
- [ ] Configure reverse proxy (nginx/Apache)
- [ ] Set up service management (systemd/Docker)
- [ ] Configure firewall rules

## 🔄 Upgrade Process

1. **Backup**: Save current configuration
2. **Stop**: Gracefully stop applications
3. **Replace**: Update JAR files
4. **Configure**: Update configurations if needed
5. **Test**: Verify functionality
6. **Start**: Launch updated applications

## 📞 Support & Maintenance

### Log Rotation
```bash
# Using logrotate (Linux)
/etc/logrotate.d/banking-app
```

### Backup Strategy
- Configuration files: Daily backup
- Database: Hourly snapshots (if using persistent DB)
- Application logs: Weekly rotation

### Monitoring Recommendations
- Application response times
- Database connection pool usage
- JVM memory and CPU usage
- Error rates and exceptions
