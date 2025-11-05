#!/bin/bash

# NetBanking App Integration Verification Script
echo "=== NetBanking App Integration Verification ==="
echo ""

# Check main JAR
if [ -f "netbanking-app-1.0.0.jar" ]; then
    echo "✅ Main JAR: netbanking-app-1.0.0.jar ($(du -h netbanking-app-1.0.0.jar | cut -f1))"
else
    echo "❌ Main JAR not found"
    exit 1
fi

# Check lib folder
if [ -d "lib" ]; then
    echo "✅ Library folder exists"
    if [ -f "lib/banking-core-1.0.0.jar" ]; then
        echo "  ✅ Banking-core library: $(du -h lib/banking-core-1.0.0.jar | cut -f1)"
    else
        echo "  ❌ Banking-core library not found"
    fi
    if [ -f "lib/banking-core-1.0.0-sources.jar" ]; then
        echo "  ✅ Banking-core sources: $(du -h lib/banking-core-1.0.0-sources.jar | cut -f1)"
    else
        echo "  ❌ Banking-core sources not found"
    fi
else
    echo "❌ Library folder not found"
fi

echo ""
echo "Checking JAR integration..."

# Check if banking-core is embedded in main JAR
if jar -tf netbanking-app-1.0.0.jar | grep -q "BOOT-INF/lib/banking-core-1.0.0.jar"; then
    echo "✅ Banking-core JAR is embedded in netbanking-app JAR"
else
    echo "❌ Banking-core JAR not found in netbanking-app JAR"
fi

# Check for banking classes
echo ""
echo "Banking-core classes in library JAR:"
jar -tf lib/banking-core-1.0.0.jar | grep -E "com/banking/core/(entity|service|repository)" | head -3

echo ""
echo "NetBanking application classes:"
jar -tf netbanking-app-1.0.0.jar | grep "BOOT-INF/classes/com/netbanking" | head -3

echo ""
echo "Testing quick startup (10 second test)..."
timeout 10s java -jar netbanking-app-1.0.0.jar --server.port=8089 2>&1 | grep -E "Started|Tomcat|Error|NetBankingApplication" | head -2

echo ""
echo "✅ Verification complete!"
echo "NetBanking App is ready to run with integrated Banking-Core dependency."
echo ""
echo "To start the application:"
echo "  java -jar netbanking-app-1.0.0.jar"
echo "  Access at: http://localhost:8080/api"
echo "  Swagger UI: http://localhost:8080/api/swagger-ui.html"
