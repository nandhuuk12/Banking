@echo off
REM NetBanking Application Startup Script for Windows

echo Starting NetBanking Application...

REM Get the directory of this batch file
set "DIR=%~dp0"
set "BASE_DIR=%DIR%.."

REM Java and JAR settings
set "JAVA_OPTS=-Xmx1g -Xms512m"
set "JAR_FILE=%BASE_DIR%\netbanking-app\netbanking-app-1.0.0.jar"
set "CONFIG_FILE=%BASE_DIR%\config\netbanking-app-application.properties"

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

echo.
echo NetBanking Application should be available at:
echo - Main API: http://localhost:8080/api
echo - Swagger UI: http://localhost:8080/api/swagger-ui.html
echo - H2 Console: http://localhost:8080/api/h2-console
echo.
pause
