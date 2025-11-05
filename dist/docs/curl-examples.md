# Banking System API - cURL Examples

This document provides example cURL commands to interact with the Banking System API.

## Base URL
```bash
BASE_URL="http://localhost:8080/api"
```

## Authentication

### 1. Login (Regular User)
```bash
curl -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user1@bank.com",
    "password": "User@123"
  }'
```

### 2. Login (Admin User)
```bash
curl -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@bank.com",
    "password": "Admin@123"
  }'
```

### 3. Store JWT Token
```bash
# After login, extract and store the token
JWT_TOKEN=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user1@bank.com",
    "password": "User@123"
  }' | jq -r '.accessToken')

echo "JWT Token: $JWT_TOKEN"
```

### 4. Refresh Token
```bash
curl -X POST "$BASE_URL/auth/refresh" \
  -H "Authorization: Bearer $JWT_TOKEN"
```

## Account Management

### 1. Get User Accounts
```bash
curl -X GET "$BASE_URL/accounts" \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 2. Get Account Summary
```bash
curl -X GET "$BASE_URL/accounts/summary" \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 3. Get Specific Account Details
```bash
curl -X GET "$BASE_URL/accounts/123401000000001" \
  -H "Authorization: Bearer $JWT_TOKEN"
```

### 4. Create New Account
```bash
curl -X POST "$BASE_URL/accounts" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "accountType": "SAVINGS",
    "branchIfsc": "BANK0001234",
    "initialDeposit": 1000.00
  }'
```

### 5. Deposit Money
```bash
curl -X POST "$BASE_URL/accounts/123401000000001/deposit" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 500.00,
    "narration": "Cash deposit"
  }'
```

### 6. Withdraw Money
```bash
curl -X POST "$BASE_URL/accounts/123401000000001/withdraw" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 200.00,
    "narration": "ATM withdrawal"
  }'
```

### 7. Transfer Money
```bash
curl -X POST "$BASE_URL/accounts/transfer" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "fromAccountNumber": "123401000000001",
    "toAccountNumber": "567801000000001",
    "amount": 100.00,
    "narration": "Transfer to friend"
  }'
```

### 8. Get Account Balance
```bash
curl -X GET "$BASE_URL/accounts/123401000000001/balance" \
  -H "Authorization: Bearer $JWT_TOKEN"
```

## Admin Operations (Requires ROLE_ADMIN)

### 1. Block Account
```bash
curl -X PUT "$BASE_URL/accounts/123401000000001/block?reason=Suspicious%20activity" \
  -H "Authorization: Bearer $ADMIN_JWT_TOKEN"
```

### 2. Unblock Account
```bash
curl -X PUT "$BASE_URL/accounts/123401000000001/unblock" \
  -H "Authorization: Bearer $ADMIN_JWT_TOKEN"
```

## Health Check

### 1. Application Health
```bash
curl -X GET "$BASE_URL/actuator/health"
```

## Sample Full Workflow

```bash
#!/bin/bash

# Banking System API Test Script
BASE_URL="http://localhost:8080/api"

echo "=== Banking System API Test ==="
echo

# 1. Health Check
echo "1. Checking application health..."
curl -s "$BASE_URL/actuator/health" | jq .
echo

# 2. Login
echo "2. Logging in as user..."
JWT_TOKEN=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user1@bank.com",
    "password": "User@123"
  }' | jq -r '.accessToken')

if [ "$JWT_TOKEN" != "null" ] && [ "$JWT_TOKEN" != "" ]; then
    echo "Login successful!"
else
    echo "Login failed!"
    exit 1
fi
echo

# 3. Get Account Summary
echo "3. Getting account summary..."
curl -s -X GET "$BASE_URL/accounts/summary" \
  -H "Authorization: Bearer $JWT_TOKEN" | jq .
echo

# 4. Get Account Balance
echo "4. Getting account balance..."
curl -s -X GET "$BASE_URL/accounts/123401000000001/balance" \
  -H "Authorization: Bearer $JWT_TOKEN"
echo
echo

# 5. Make a deposit
echo "5. Making a deposit..."
curl -s -X POST "$BASE_URL/accounts/123401000000001/deposit" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 100.00,
    "narration": "Test deposit"
  }' | jq .
echo

# 6. Check balance after deposit
echo "6. Checking balance after deposit..."
curl -s -X GET "$BASE_URL/accounts/123401000000001/balance" \
  -H "Authorization: Bearer $JWT_TOKEN"
echo
echo

echo "=== Test completed ==="
```

## Error Handling

### Common HTTP Status Codes
- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Authentication required or failed
- `403 Forbidden` - Access denied
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

### Example Error Response
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "timestamp": 1699123456789,
  "path": "/api/accounts/transfer"
}
```

## Notes

1. **JWT Token Expiry**: Access tokens expire after 24 hours. Use the refresh token endpoint to get a new access token.

2. **Sample Data**: The application comes with pre-loaded sample data:
   - User: `user1@bank.com` / `User@123`
   - Admin: `admin@bank.com` / `Admin@123`
   - Sample accounts with existing balances

3. **Rate Limiting**: API requests may be rate-limited in production. Check response headers for rate limit information.

4. **CORS**: The API supports CORS for cross-origin requests from web applications.

5. **API Documentation**: Interactive API documentation is available at `/swagger-ui.html` when the application is running.
