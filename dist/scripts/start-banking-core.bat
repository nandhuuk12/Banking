@echo off
REM Banking Core Startup Script for Windows

echo Starting Banking Core Application...

REM Get the directory of this batch file
set "DIR=%~dp0"
set "BASE_DIR=%DIR%.."

REM Java and JAR settings
set "JAVA_OPTS=-Xmx1g -Xms512m"
set "JAR_FILE=%BASE_DIR%\banking-core\banking-core-1.0.0-boot.jar"
set "CONFIG_FILE=%BASE_DIR%\config\banking-core-application.properties"

REM Check if JAR exists
if not exist "%JAR_FILE%" (
    echo Error: JAR file not found at %JAR_FILE%
    pause
    exit /b 1
)

REM Check if config exists
if not exist "%CONFIG_FILE%" (
    echo Warning: Config file not found at %CONFIG_FILE%
    echo Using default configuration
    java %JAVA_OPTS% -jar "%JAR_FILE%" --spring.profiles.active=dev
) else (
    echo Using configuration from %CONFIG_FILE%
    java %JAVA_OPTS% -jar "%JAR_FILE%" --spring.config.location="%CONFIG_FILE%" --spring.profiles.active=dev
)

pause
