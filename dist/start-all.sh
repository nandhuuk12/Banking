#!/bin/bash

# Banking System Launcher
echo "=== Banking System Launcher ==="
echo ""
echo "Choose an option:"
echo "1. Start Banking Core (standalone service)"
echo "2. Start NetBanking Application (web app)"
echo "3. Start both applications"
echo "4. Exit"
echo ""
read -p "Enter your choice (1-4): " choice

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

case $choice in
    1)
        echo "Starting Banking Core..."
        "$DIR/scripts/start-banking-core.sh"
        ;;
    2)
        echo "Starting NetBanking Application..."
        "$DIR/scripts/start-netbanking-app.sh"
        ;;
    3)
        echo "Starting both applications..."
        echo "Starting Banking Core in background..."
        "$DIR/scripts/start-banking-core.sh" &
        sleep 10
        echo "Starting NetBanking Application..."
        "$DIR/scripts/start-netbanking-app.sh"
        ;;
    4)
        echo "Exiting..."
        exit 0
        ;;
    *)
        echo "Invalid choice. Please run the script again."
        exit 1
        ;;
esac
