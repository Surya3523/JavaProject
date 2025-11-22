import java.util.Scanner;
import java.util.Random;
import java.text.SimpleDateFormat;
import java.util.Date;

class Main {

    public static void clearScreen() {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    public static String generateAlphaNumericOTP() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder otp = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 6; i++) otp.append(chars.charAt(r.nextInt(chars.length())));
        return otp.toString();
    }

    public static String now() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    // ===================== ACCOUNT CLASS =====================
    static class BankAccount {
        protected String accountType;
        protected String accountName;
        protected long accountNumber;
        protected String bankName;
        protected double balance;

        protected String pin = null;
        protected int pinLength = 4;

        protected String phoneNumber; // phone number for OTP validation

        protected String[] txHistory = new String[10];
        protected int txIndex = 0;
        protected int txCount = 0;

        public BankAccount(String type, String name, long number, String bankName) {
            this.accountType = type;
            this.accountName = name;
            this.accountNumber = number;
            this.bankName = bankName;
            this.balance = 0.0;
        }

        public long getAccountNumber() { return accountNumber; }

        public void addTransaction(String record) {
            txHistory[txIndex] = now() + " | " + record;
            txIndex = (txIndex + 1) % txHistory.length;
            if (txCount < txHistory.length) txCount++;
        }

        public void printTransactions() {
            if (txCount == 0) {
                System.out.println("No transactions yet.");
                return;
            }
            System.out.println("\n----- LAST TRANSACTIONS -----");
            int start = (txIndex - txCount + txHistory.length) % txHistory.length;
            for (int i = 0; i < txCount; i++) {
                int idx = (start + i) % txHistory.length;
                System.out.println(txHistory[idx]);
            }
            System.out.println("--------------------------------");
        }

        public void deposit(double amount) {
            if (amount > 0) {
                balance += amount;
                System.out.println("Deposited " + amount);
                addTransaction("Deposit : +" + amount + " | Bal: " + balance);
            } else System.out.println("Invalid amount!");
        }

        public boolean withdraw(double amount) {
            if (amount <= 0) {
                System.out.println("Invalid withdrawal amount!");
                return false;
            }
            if (amount > balance) {
                System.out.println("Insufficient balance!");
                return false;
            }
            balance -= amount;
            addTransaction("Withdraw : -" + amount + " | Bal: " + balance);
            return true;
        }

        public double getBalance() { return balance; }

        public void displayAccount() {
            System.out.println("\n----- ACCOUNT DETAILS -----");
            System.out.println("Bank Name    : " + bankName);
            System.out.println("Account Type : " + accountType);
            System.out.println("Nickname     : " + accountName);
            System.out.println("Account No   : " + accountNumber);
            System.out.println("Balance      : " + balance);
            System.out.println("PIN Length   : " + pinLength + "-digit");
            System.out.println("Phone Number : " + phoneNumber);
            System.out.println("---------------------------");
        }
    }

    static class SavingsAccount extends BankAccount {
        public SavingsAccount(String name, long num, String bankName) {
            super("Savings Account", name, num, bankName);
        }
    }

    static class CurrentAccount extends BankAccount {
        public CurrentAccount(String name, long num, String bankName) {
            super("Current Account", name, num, bankName);
        }
    }

    // ===================== USER CLASS =====================
    static class User {
        private String email, username, password;
        private int age;
        private BankAccount[] accounts = new BankAccount[50];
        private int accCount = 0;

        public User(String email, String username, String password, int age) {
            this.email = email;
            this.username = username;
            this.password = password;
            this.age = age;
        }

        public boolean checkPassword(String pass) { return pass.equals(password); }
        public String getUsername() { return username; }
        public void setUsername(String u) { username = u; }
        public void setPassword(String p) { password = p; }

        public void addAccount(BankAccount acc) { accounts[accCount++] = acc; }
        public int getAccCount() { return accCount; }
        public BankAccount[] getAccounts() { return accounts; }

        public BankAccount getAccountByNumber(long num) {
            for (int i = 0; i < accCount; i++)
                if (accounts[i].getAccountNumber() == num) return accounts[i];
            return null;
        }
    }

    // ===================== AUTH SERVICE =====================
    static class AuthService {
        User[] users = new User[100];
        int userCount = 0;

        public void signUp(Scanner sc) {
            clearScreen();
            System.out.println("----- SIGN UP -----");

            System.out.print("Enter Age: ");
            int age = sc.nextInt(); sc.nextLine();
            if (age < 18) { System.out.println("Age must be 18+"); return; }

            String email = "";
            while (true) {
                System.out.print("Enter Email (lowercase + @gmail.com): ");
                email = sc.nextLine();
                if (email.matches("[a-z0-9]+@gmail\\.com")) break;
                System.out.println("Invalid email!");
            }

            // OTP
            for (int attempts = 0; attempts < 3; attempts++) {
                String otp = generateAlphaNumericOTP();
                System.out.println("Your OTP: " + otp);
                System.out.print("Enter OTP: ");
                if (otp.equals(sc.nextLine())) {
                    System.out.println("OTP Verified!");
                    break;
                }
                System.out.println("Incorrect OTP!");
                if (attempts == 2) return;
            }

            // Username
            String uname = "";
            int uAttempts = 0;
            while (uAttempts < 3) {
                System.out.print("Enter Username (letters & digits only): ");
                uname = sc.nextLine();
                if (uname.matches("[A-Za-z0-9]+")) break;
                uAttempts++;
                System.out.println("Invalid Username! Attempts left: " + (3 - uAttempts));
                if (uAttempts == 3) return;
            }

            // Password
            String pass = "", cpass = "";
            int pAttempts = 0;
            while (pAttempts < 3) {
                System.out.print("Enter Password (letters, digits, special chars): ");
                pass = sc.nextLine();
                if (!pass.matches("(?=.*[A-Za-z])(?=.*[0-9])(?=.*[@#$%^&+=!]).+")) {
                    pAttempts++;
                    System.out.println("Invalid Password! Attempts left: " + (3 - pAttempts));
                    if (pAttempts == 3) return;
                    continue;
                }
                System.out.print("Confirm Password: ");
                cpass = sc.nextLine();
                if (pass.equals(cpass)) break;
                pAttempts++;
                System.out.println("Passwords do not match! Attempts left: " + (3 - pAttempts));
                if (pAttempts == 3) return;
            }

            users[userCount++] = new User(email, uname, pass, age);
            System.out.println("Signup Successful! Please login.");
        }

        // ===================== ADD BANK ACCOUNT =====================
        public void createBankAccount(Scanner sc, User user) {

            clearScreen();
            System.out.println("----- ADD BANK ACCOUNT -----");

            System.out.println("Select Bank:");
            System.out.println("1. SBI");
            System.out.println("2. HDFC");
            System.out.println("3. ICICI");
            System.out.println("4. AXIS");
            System.out.println("5. KOTAK");
            System.out.println("6. OTHER");
            System.out.print("Choice: ");
            int bc = sc.nextInt(); sc.nextLine();

            String bankName = "";
            switch (bc) {
                case 1: bankName = "SBI"; break;
                case 2: bankName = "HDFC"; break;
                case 3: bankName = "ICICI"; break;
                case 4: bankName = "AXIS"; break;
                case 5: bankName = "KOTAK"; break;
                case 6:
                    System.out.print("Enter Bank Name: ");
                    bankName = sc.nextLine();
                    break;
                default:
                    System.out.print("Enter Bank Name: ");
                    bankName = sc.nextLine();
            }

            System.out.println("1. Savings");
            System.out.println("2. Current");
            System.out.print("Choose: ");
            int type = sc.nextInt(); sc.nextLine();

            // Nickname
            String nick = "";
            while (true) {
                System.out.print("Enter Account Nickname: ");
                nick = sc.nextLine();
                boolean exists = false;
                for (int i = 0; i < user.getAccCount(); i++)
                    if (user.getAccounts()[i].accountName.equalsIgnoreCase(nick)) exists = true;
                if (!exists) break;
                System.out.println("Nickname already used!");
            }

            // Account number
            long accNum = 0;
            while (true) {
                System.out.print("Enter Account Number: ");
                long a = sc.nextLong(); sc.nextLine();
                System.out.print("Re-enter Account Number: ");
                long b = sc.nextLong(); sc.nextLine();
                if (a != b) {
                    System.out.println("Mismatch!");
                    continue;
                }
                accNum = a;

                boolean exists = false;
                for (int i = 0; i < user.getAccCount(); i++)
                    if (user.getAccounts()[i].accountNumber == accNum) exists = true;

                if (!exists) break;
                System.out.println("Account number already exists!");
            }

            // PIN
            int pinLen = 0;
            while (true) {
                System.out.print("Choose PIN length (4/6): ");
                String s = sc.nextLine();
                if (s.equals("4") || s.equals("6")) {
                    pinLen = Integer.parseInt(s);
                    break;
                }
            }

            String pin = "";
            while (true) {
                System.out.print("Enter " + pinLen + "-digit PIN: ");
                pin = sc.nextLine();
                if (!pin.matches("\\d{" + pinLen + "}")) {
                    System.out.println("Invalid PIN!");
                    continue;
                }
                System.out.print("Confirm PIN: ");
                if (pin.equals(sc.nextLine())) break;
                System.out.println("Mismatch!");
            }

            // Phone Number
            String phone = "";
            while (true) {
                System.out.print("Enter Phone Number: ");
                phone = sc.nextLine();
                if (phone.matches("[6-9][0-9]{9}")) break;
                System.out.println("Invalid phone number!");
            }

            // Phone OTP (5 attempts)
            for (int attempts = 0; attempts < 5; attempts++) {
                int otp = new Random().nextInt(9000) + 1000;
                System.out.println("Phone OTP: " + otp);
                System.out.print("Enter OTP: ");
                if (otp == sc.nextInt()) { sc.nextLine(); break; }
                sc.nextLine();
                System.out.println("Incorrect. Attempts left: " + (4 - attempts));
                if (attempts == 4) return;
            }

            BankAccount acc;
            if (type == 1) acc = new SavingsAccount(nick, accNum, bankName);
            else acc = new CurrentAccount(nick, accNum, bankName);

            acc.pin = pin;
            acc.pinLength = pinLen;
            acc.phoneNumber = phone;

            // INITIAL BALANCE LOGIC
            Random rand = new Random();

            if (type == 1) {
                // Savings Account: 500 – 10000
                acc.balance = 500 + rand.nextInt(9501);
            } else {
                // Current Account – Bank-wise minimum balance
                if (bankName.equalsIgnoreCase("SBI")) {
                    acc.balance = 5000 + rand.nextInt(5001); // 5000 – 10000
                }
                else if (bankName.equalsIgnoreCase("HDFC") ||
                         bankName.equalsIgnoreCase("ICICI") ||
                         bankName.equalsIgnoreCase("AXIS") ||
                         bankName.equalsIgnoreCase("KOTAK")) {

                    acc.balance = 10000 + rand.nextInt(15001); // 10000 – 25000
                }
                else {
                    // Other Banks (per your last instruction): 5000 – 10000
                    acc.balance = 5000 + rand.nextInt(5001);
                }
            }

            user.addAccount(acc);
            System.out.println("Account Created Successfully!");
        }

        public BankAccount findAny(long num) {
            for (int i = 0; i < userCount; i++) {
                BankAccount a = users[i].getAccountByNumber(num);
                if (a != null) return a;
            }
            return null;
        }

        public User login(Scanner sc) {
            clearScreen();
            System.out.println("----- LOGIN -----");
            if (userCount == 0) {
                System.out.println("No users found!");
                return null;
            }

            System.out.print("Enter Username: ");
            String u = sc.nextLine();
            System.out.print("Enter Password: ");
            String p = sc.nextLine();

            for (int i = 0; i < userCount; i++) {
                if (users[i].getUsername().equals(u) &&
                    users[i].checkPassword(p))
                    return users[i];
            }

            System.out.println("Invalid Credentials!");
            return null;
        }
    }

    // ===================== MAIN MENU =====================
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AuthService auth = new AuthService();

        while (true) {
            clearScreen();
            System.out.println("----- MAIN MENU -----");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choice: ");
            int ch = sc.nextInt(); sc.nextLine();

            if (ch == 1) auth.signUp(sc);
            else if (ch == 2) {
                User u = auth.login(sc);
                if (u != null) userMenu(sc, auth, u);
            }
            else if (ch == 3) return;
        }
    }

    // ===================== USER MENU =====================
    public static void userMenu(Scanner sc, AuthService auth, User user) {

        while (true) {
            clearScreen();
            System.out.println("----- USER MENU -----");
            System.out.println("1. View Accounts");
            System.out.println("2. Operate Account");
            System.out.println("3. Add Account");
            System.out.println("4. Edit Profile");
            System.out.println("5. Logout");
            System.out.print("Choice: ");
            int ch = sc.nextInt(); sc.nextLine();

            if ((ch == 1 || ch == 2) && user.getAccCount() == 0) {
                System.out.println("No accounts found!");
                sc.nextLine(); continue;
            }

            if (ch == 1) {
                for (int i = 0; i < user.getAccCount(); i++)
                    user.getAccounts()[i].displayAccount();
                sc.nextLine();
            }
            else if (ch == 2) {
                System.out.print("Enter Account Number: ");
                long n = sc.nextLong(); sc.nextLine();
                BankAccount a = user.getAccountByNumber(n);
                if (a != null) accountOperations(sc, auth, a);
                else System.out.println("Account not found!");
                sc.nextLine();
            }
            else if (ch == 3) auth.createBankAccount(sc, user);
            else if (ch == 4) editProfile(sc, user);
            else if (ch == 5) return;
        }
    }

    // ===================== EDIT PROFILE =====================
    public static void editProfile(Scanner sc, User user) {

        clearScreen();
        System.out.println("----- EDIT PROFILE -----");
        System.out.println("1. Change Username");
        System.out.println("2. Change Password");
        System.out.println("3. Back");
        System.out.print("Choice: ");
        int ch = sc.nextInt(); sc.nextLine();

        if (ch == 1) {
            System.out.print("New Username: ");
            String u = sc.nextLine();
            if (u.matches("[A-Za-z0-9]+")) user.setUsername(u);
            else System.out.println("Invalid username!");
        }
        else if (ch == 2) {
            System.out.print("Enter old password: ");
            String op = sc.nextLine();
            if (!user.checkPassword(op)) {
                System.out.println("Wrong Password!");
                return;
            }

            System.out.print("Enter new password: ");
            String np = sc.nextLine();

            if (!np.matches("(?=.*[A-Za-z])(?=.*[0-9])(?=.*[@#$%^&+=!]).+")) {
                System.out.println("Weak password!");
                return;
            }

            System.out.print("Confirm new password: ");
            if (!np.equals(sc.nextLine())) {
                System.out.println("Mismatch!");
                return;
            }

            user.setPassword(np);
            System.out.println("Password Updated!");
        }
    }

    // ===================== ACCOUNT OPERATIONS =====================
    public static void accountOperations(Scanner sc, AuthService auth, BankAccount acc) {

        while (true) {
            clearScreen();
            System.out.println("---- ACCOUNT OPERATIONS ----");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw (PIN & min balance rule for Current)");
            System.out.println("3. Check Balance (PIN)");
            System.out.println("4. View Details");
            System.out.println("5. Transfer (PIN)");
            System.out.println("6. Transaction History");
            System.out.println("7. Change PIN");
            System.out.println("8. Back");
            System.out.print("Choice: ");
            int ch = sc.nextInt(); sc.nextLine();

            if (ch == 1) {
                System.out.print("Amount: ");
                acc.deposit(sc.nextDouble());
                sc.nextLine();
            }

            else if (ch == 2) {
                if (!verifyPin(sc, acc)) continue;

                System.out.print("Amount: ");
                double amt = sc.nextDouble();
                sc.nextLine();

                double minBal = 0;

                // Apply minimum balance rule ONLY for current accounts
                if (acc.accountType.equalsIgnoreCase("Current Account")) {

                    if (acc.bankName.equalsIgnoreCase("SBI")) {
                        minBal = 5000;
                    }
                    else if (acc.bankName.equalsIgnoreCase("HDFC") ||
                             acc.bankName.equalsIgnoreCase("ICICI") ||
                             acc.bankName.equalsIgnoreCase("AXIS") ||
                             acc.bankName.equalsIgnoreCase("KOTAK")) {

                        minBal = 10000;
                    }
                    else {
                        // Other bank minimum per your provided logic
                        minBal = 5000;
                    }

                    // Check if withdrawal violates minimum balance
                    if (acc.balance - amt < minBal) {
                        System.out.println("Minimum balance rule violated.");
                        System.out.println("Required Minimum Balance : " + minBal);
                        System.out.println("Withdrawal Denied.");
                        continue;
                    }
                }

                // If here -> withdrawal is allowed
                if (acc.withdraw(amt)) {
                    System.out.println("Withdrawn Successfully!");
                    System.out.println("Remaining Balance: " + acc.getBalance());
                }
            }

            else if (ch == 3) {
                if (!verifyPin(sc, acc)) continue;
                System.out.println("Balance: " + acc.getBalance());
            }

            else if (ch == 4) {
                acc.displayAccount();
            }

            else if (ch == 5) {
                if (!verifyPin(sc, acc)) continue;
                System.out.print("Receiver Account Number: ");
                long num = sc.nextLong(); sc.nextLine();
                BankAccount rcv = auth.findAny(num);
                if (rcv == null) {
                    System.out.println("Receiver not found.");
                    continue;
                }
                System.out.print("Amount: ");
                double amt = sc.nextDouble(); sc.nextLine();

                // For transfer, if acc is Current Account, ensure min balance after transfer
                if (acc.accountType.equalsIgnoreCase("Current Account")) {
                    double minBal = 0;
                    if (acc.bankName.equalsIgnoreCase("SBI")) minBal = 5000;
                    else if (acc.bankName.equalsIgnoreCase("HDFC") ||
                             acc.bankName.equalsIgnoreCase("ICICI") ||
                             acc.bankName.equalsIgnoreCase("AXIS") ||
                             acc.bankName.equalsIgnoreCase("KOTAK")) minBal = 10000;
                    else minBal = 5000;

                    if (acc.getBalance() - amt < minBal) {
                        System.out.println("Transfer would violate minimum balance. Operation denied.");
                        continue;
                    }
                }

                if (acc.withdraw(amt)) {
                    rcv.balance += amt;
                    acc.addTransaction("Transfer Out: -" + amt + " | Bal: " + acc.getBalance());
                    rcv.addTransaction("Transfer In : +" + amt);
                    System.out.println("Transfer Successful!");
                }
            }

            else if (ch == 6) {
                acc.printTransactions();
            }

            else if (ch == 7) {
                changePin(sc, acc);
            }

            else if (ch == 8) return;

            sc.nextLine();
        }
    }

    // ===================== CHANGE PIN =====================
    public static void changePin(Scanner sc, BankAccount acc) {

        System.out.println("----- CHANGE PIN -----");

        System.out.print("Enter OLD PIN: ");
        String old = sc.nextLine();
        if (!old.equals(acc.pin)) {
            System.out.println("Incorrect Old PIN!");
            return;
        }

        int otp = new Random().nextInt(9000) + 1000;
        System.out.println("OTP sent to registered phone: " + otp);

        System.out.print("Enter OTP: ");
        if (sc.nextInt() != otp) {
            sc.nextLine();
            System.out.println("Incorrect OTP!");
            return;
        }
        sc.nextLine();

        String newPin = "";
        while (true) {
            System.out.print("Enter NEW " + acc.pinLength + "-digit PIN: ");
            newPin = sc.nextLine();
            if (!newPin.matches("\\d{" + acc.pinLength + "}")) {
                System.out.println("Invalid PIN format!");
                continue;
            }
            System.out.print("Confirm NEW PIN: ");
            if (newPin.equals(sc.nextLine())) break;
            System.out.println("PIN mismatch!");
        }

        acc.pin = newPin;
        System.out.println("PIN Changed Successfully!");
    }

    public static boolean verifyPin(Scanner sc, BankAccount acc) {
        System.out.print("Enter " + acc.pinLength + "-digit PIN: ");
        String p = sc.nextLine();
        if (!p.matches("\\d{" + acc.pinLength + "}")) {
            System.out.println("Invalid PIN format!");
            return false;
        }
        if (!p.equals(acc.pin)) {
            System.out.println("Incorrect PIN!");
            return false;
        }
        return true;
    }
}
