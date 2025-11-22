# Bank Account Management System (Java Console Application)

A complete console-based banking system developed using **Core Java**, designed to simulate real-world banking operations with a strong focus on security, modularity, and clean Object-Oriented Programming (OOP) structure.  
This project supports **multi-user management, multiple bank accounts**, OTP verification, PIN security, and bank-specific minimum balance rules.

---

## 🚀 Features

### 🧑‍💼 User & Authentication
- New user **Signup with email verification**  
  - 6-character **alphanumeric OTP**
  - Only lowercase Gmail allowed (`something@gmail.com`)
- **Secure Login** with:
  - Username validation (letters + numbers)
  - Password validation (letters + numbers + special characters)
  - Password confirmation and multiple attempts allowed
- Profile options:
  - Change username  
  - Change password with full validation rules  

---

## 🏦 Bank Account Features
- A user can create **multiple bank accounts**  
- Supported account types:
  - **Savings Account**
  - **Current Account**
- Bank selection:
  - SBI, HDFC, ICICI, AXIS, KOTAK, or “Other Bank”
- Must enter:
  - Bank name  
  - Account nickname  
  - Re-confirm account number  
  - 4 or 6 digit PIN  
  - Phone number verification with OTP  

---

## 🔐 Security Implemented
- PIN required for:
  - Withdraw  
  - Balance inquiry  
  - Money transfer  
- **Change PIN** requires:
  - Old PIN  
  - Phone OTP  
  - New PIN confirmation  
- Email signup verification  
- Phone OTP during account creation  
- Case-sensitive OTP system  

---

## 💸 Banking Operations

### ✔ Deposit  
Add money into the selected bank account.

### ✔ Withdrawal  
Includes:
- PIN verification  
- Bank-specific **minimum balance rule** (for Current Accounts):
  - SBI → 5000 minimum  
  - HDFC/ICICI/AXIS/KOTAK → 10000 minimum  
  - Other banks → 5000 minimum  
- If withdrawing violates minimum balance → **withdrawal denied**

### ✔ Net Banking (Money Transfer)
- Transfer money using receiver’s account number  
- Transfer also checks **minimum balance rule**  

### ✔ Check Balance (PIN Required)

### ✔ View Account Details

### ✔ View Transaction History
- Latest **10 transactions**
- Timestamp included for each operation

---

## 🧮 Initial Account Balance Logic

### Savings Account
- Automatically assigned random balance between **500 – 10000**

### Current Account
Bank-specific initial balance:
- SBI → **5000 – 10000**
- HDFC / ICICI / AXIS / KOTAK → **10000 – 25000**
- Other banks → **5000 – 10000**

---

## 📁 Project Structure

