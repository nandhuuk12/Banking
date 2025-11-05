#!/bin/bash

# NetBanking Application Startup Script
echo "Starting NetBanking Application..."

# Set the directory to the script location
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
BASE_DIR="$(dirname "$DIR")"

# Java and JAR settings
JAVA_OPTS="-Xmx1g -Xms512m"
JAR_FILE="$BASE_DIR/netbanking-app/netbanking-app-1.0.0.jar"
CONFIG_FILE="$BASE_DIR/config/netbanking-app-application.properties"

# Check if JAR exists
if [ ! -f "$JAR_FILE" ]; then
    echo "Error: JAR file not found at $JAR_FILE"
    exit 1
fi

# Check if config exists
if [ ! -f "$CONFIG_FILE" ]; then
    echo "Warning: Config file not found at $CONFIG_FILE"
    echo "Using default configuration"
    java $JAVA_OPTS -jar "$JAR_FILE" --spring.profiles.active=dev
else
    echo "Using configuration from $CONFIG_FILE"
    java $JAVA_OPTS -jar "$JAR_FILE" --spring.config.location="$CONFIG_FILE" --spring.profiles.active=dev
fi

echo "NetBanking Application should be available at:"
echo "- Main API: http://localhost:8080/api"
echo "- Swagger UI: http://localhost:8080/api/swagger-ui.html"
echo "- H2 Console: http://localhost:8080/api/h2-console"
