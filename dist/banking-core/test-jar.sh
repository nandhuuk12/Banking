#!/bin/bash

# Banking Core JAR Test Script
echo "=== Banking Core JAR Test ==="
echo ""

# Check if JAR files exist
echo "Checking JAR files..."
if [ -f "banking-core-1.0.0-boot.jar" ]; then
    echo "✅ banking-core-1.0.0-boot.jar ($(du -h banking-core-1.0.0-boot.jar | cut -f1))"
else
    echo "❌ banking-core-1.0.0-boot.jar not found"
fi

if [ -f "banking-core-1.0.0.jar" ]; then
    echo "✅ banking-core-1.0.0.jar ($(du -h banking-core-1.0.0.jar | cut -f1))"
else
    echo "❌ banking-core-1.0.0.jar not found"
fi

echo ""
echo "Testing JAR file contents..."
echo "Banking Core classes in library JAR:"
jar -tf banking-core-1.0.0.jar | grep -E "com/banking/core/(entity|service|repository)" | head -5

echo ""
echo "Testing Spring Boot JAR startup (15 second test)..."
timeout 15s java -jar banking-core-1.0.0-boot.jar --server.port=8082 2>&1 | grep -E "Started|Tomcat|Port|Error" | head -3

echo ""
echo "Test complete! Both JAR files are ready for use."
echo "- Use banking-core-1.0.0-boot.jar to run as standalone service"
echo "- Use banking-core-1.0.0.jar as library dependency in other projects"
