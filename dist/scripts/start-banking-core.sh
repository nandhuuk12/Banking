#!/bin/bash

# Banking Core Startup Script
echo "Starting Banking Core Application..."

# Set the directory to the script location
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
BASE_DIR="$(dirname "$DIR")"

# Java and JAR settings
JAVA_OPTS="-Xmx1g -Xms512m"
JAR_FILE="$BASE_DIR/banking-core/banking-core-1.0.0-boot.jar"
CONFIG_FILE="$BASE_DIR/config/banking-core-application.properties"

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
