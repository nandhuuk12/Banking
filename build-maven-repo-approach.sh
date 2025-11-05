#!/bin/bash

# Banking System Build Script - Maven Repository Approach
# This script demonstrates using banking-core from Maven local repository

set -e

echo "======================================================"
echo "Banking System Build - Maven Repository Approach"
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

# Step 1: Install Banking Core to local Maven repository
print_step "Installing Banking Core to Maven local repository..."

cd banking-core
if mvn clean install -DskipTests; then
    print_success "Banking Core installed to Maven repository"
else
    print_error "Failed to install Banking Core"
    exit 1
fi

# Show Maven repository location
echo "Banking Core installed to Maven repository at:"
ls -la ~/.m2/repository/com/banking/banking-core/1.0.0/
echo

# Step 2: Use alternative pom.xml for NetBanking App
print_step "Setting up NetBanking Application to use Maven repository dependency..."

cd ../netbanking-app

# Backup current pom.xml and use maven-repo version
cp pom.xml pom-system-path.xml.bak
cp pom-maven-repo.xml pom.xml

print_success "Switched to Maven repository dependency configuration"

# Step 3: Build NetBanking Application (using Maven repository dependency)
print_step "Building NetBanking Application with Maven repository dependency..."

if mvn clean package -DskipTests; then
    print_success "NetBanking Application built successfully"
else
    print_error "Failed to build NetBanking Application"
    # Restore original pom.xml
    cp pom-system-path.xml.bak pom.xml
    exit 1
fi

# Display final JAR
echo "Final NetBanking Application JAR:"
ls -la target/netbanking-app-1.0.0.jar
echo

print_success "Build completed successfully using Maven repository approach!"

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
echo ""
echo "Note: The pom.xml has been updated to use Maven repository dependency."
echo "To revert to system path approach, run:"
echo "cd netbanking-app && cp pom-system-path.xml.bak pom.xml"
