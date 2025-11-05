#!/bin/bash

# Banking System Build and Run Script
# This script builds the banking-core library and runs the netbanking-app

set -e  # Exit on any error

echo "======================================"
echo "Banking System Build and Run Script"
echo "======================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

print_step() {
    echo -e "\n${YELLOW}==> $1${NC}\n"
}

print_success() {
    echo -e "\n${GREEN}✓ $1${NC}\n"
}

print_error() {
    echo -e "\n${RED}✗ $1${NC}\n"
}

# Check prerequisites
print_step "Checking prerequisites..."

if ! command -v java &> /dev/null; then
    print_error "Java is not installed or not in PATH"
    exit 1
fi

if ! command -v mvn &> /dev/null; then
    print_error "Maven is not installed or not in PATH"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n1 | awk -F'"' '{print $2}')
echo "Java Version: $JAVA_VERSION"

MVN_VERSION=$(mvn --version | head -n1)
echo "$MVN_VERSION"

print_success "Prerequisites check completed"

# Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

# Build banking-core
print_step "Building Banking Core library..."

cd banking-core
if mvn clean package -DskipTests -q; then
    print_success "Banking Core built successfully"
else
    print_error "Failed to build Banking Core"
    exit 1
fi

# Check if JAR was created
if [ ! -f "target/banking-core-1.0.0.jar" ]; then
    print_error "Banking Core JAR not found"
    exit 1
fi

print_success "Banking Core JAR created: target/banking-core-1.0.0.jar"

# Copy JAR to netbanking-app/lib
print_step "Copying Banking Core JAR to NetBanking App..."

cp target/banking-core-1.0.0.jar ../netbanking-app/lib/
print_success "JAR copied to netbanking-app/lib/"

# Build netbanking-app
print_step "Building NetBanking Application..."

cd ../netbanking-app
if mvn clean package -DskipTests -q; then
    print_success "NetBanking Application built successfully"
else
    print_error "Failed to build NetBanking Application"
    exit 1
fi

# Option to run tests
read -p "Do you want to run tests? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    print_step "Running tests..."
    
    cd ../banking-core
    if mvn test -q; then
        print_success "Banking Core tests passed"
    else
        print_error "Banking Core tests failed"
    fi
    
    cd ../netbanking-app
    if mvn test -q; then
        print_success "NetBanking App tests passed"
    else
        print_error "NetBanking App tests failed"
    fi
fi

# Run the application
print_step "Starting NetBanking Application..."

echo "Application will be available at:"
echo "- Web App: http://localhost:8080/api"
echo "- Swagger UI: http://localhost:8080/api/swagger-ui.html"
echo "- H2 Console: http://localhost:8080/api/h2-console"
echo "- Health Check: http://localhost:8080/api/actuator/health"
echo ""
echo "Sample Users:"
echo "- Admin: admin@bank.com / Admin@123"
echo "- User: user1@bank.com / User@123"
echo ""
echo "Press Ctrl+C to stop the application"
echo ""

# Run the application
mvn spring-boot:run
