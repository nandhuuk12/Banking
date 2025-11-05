#!/bin/bash

# Banking System Build Script - JAR Library Approach
# This script demonstrates using banking-core as a JAR dependency

set -e

echo "======================================================"
echo "Banking System Build - JAR Library Approach"
echo "======================================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

print_step() {
    echo -e "\n${YELLOW}==> $1${NC}\n"
}

print_success() {
    echo -e "\n${GREEN}✓ $1${NC}\n"
}

print_error() {
    echo -e "\n${RED}✗ $1${NC}\n"
}

# Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

# Step 1: Build Banking Core library JAR
print_step "Building Banking Core library..."

cd banking-core
if mvn clean package -DskipTests; then
    print_success "Banking Core JAR built successfully"
else
    print_error "Failed to build Banking Core"
    exit 1
fi

# Check JAR files created
echo "JAR files created:"
ls -la target/*.jar
echo

# Step 2: Copy JAR to NetBanking App lib directory
print_step "Copying Banking Core JAR to NetBanking App..."

cp target/banking-core-1.0.0.jar ../netbanking-app/lib/
print_success "JAR copied to netbanking-app/lib/"

# Step 3: Build NetBanking Application (using system path dependency)
print_step "Building NetBanking Application with JAR dependency..."

cd ../netbanking-app
if mvn clean package -DskipTests; then
    print_success "NetBanking Application built successfully"
else
    print_error "Failed to build NetBanking Application"
    exit 1
fi

# Display final JAR
echo "Final NetBanking Application JAR:"
ls -la target/netbanking-app-1.0.0.jar
echo

print_success "Build completed successfully!"

echo "To run the NetBanking Application:"
echo "cd netbanking-app"
echo "java -jar target/netbanking-app-1.0.0.jar"
echo ""
echo "Or use Maven:"
echo "cd netbanking-app"
echo "mvn spring-boot:run"
echo ""
echo "Access at: http://localhost:8080/api"
echo "Swagger UI: http://localhost:8080/api/swagger-ui.html"
