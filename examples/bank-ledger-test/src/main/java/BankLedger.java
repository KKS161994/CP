
import java.util.Map;

// Brief: Build the core of a ledger. Accounts hold a balance. Money moves between accounts. It must
// stay correct when many transfers run at once.
// Stages
// • Stage 1: an Account with safe deposit and withdraw. No negative balance, only positive amounts.
// • Stage 2: a Bank that opens accounts and transfers between two of them.
// • Stage 3: make transfer correct under concurrency, with no lost updates and no deadlock.
// • Stage 4: record every transfer through an injected log you can mock.

// Stage 1 completed
// Stage 2 completed
//  Stage 3 start
// Stage 3: make transfer correct under concurrency, with no lost updates and no deadlock.
// Stage 4: record every transfer through an injected log you can mock.



// Demo / lightweight test exercising Stage 3 (concurrency-safe, deadlock-free)
// and Stage 4 (injected, mockable log). Placed first so `java BankLedger.java` runs it.
class BankLedgerDemo {
    public static void main(String[] args) throws InterruptedException {
        // Injected log: a simple counting fake standing in for a mock.
        java.util.concurrent.atomic.AtomicInteger transfersLogged =
                new java.util.concurrent.atomic.AtomicInteger();
        TransferLog countingLog = (from, to, amt) -> transfersLogged.incrementAndGet();

        Bank bank = new Bank(countingLog);
        bank.openAccount("A", 1000);
        bank.openAccount("B", 1000);
        int totalBefore = bank.getBalance("A") + bank.getBalance("B");

        int threadCount = 8;
        int transfersPerThread = 1000;
        Thread[] pool = new Thread[threadCount];
        for (int t = 0; t < threadCount; t++) {
            // Even threads send A->B, odd send B->A: opposite directions would
            // deadlock under role-ordered locking. They must not here.
            boolean aToB = (t % 2 == 0);
            pool[t] = new Thread(() -> {
                for (int i = 0; i < transfersPerThread; i++) {
                    try {
                        if (aToB) bank.transfer("A", "B", 1);
                        else      bank.transfer("B", "A", 1);
                    } catch (IllegalArgumentException e) {
                        // Insufficient funds can legitimately happen; ignore for the demo.
                    }
                }
            });
        }
        for (Thread th : pool) th.start();
        for (Thread th : pool) th.join();

        int totalAfter = bank.getBalance("A") + bank.getBalance("B");
        System.out.println("Total before: " + totalBefore + ", after: " + totalAfter);
        System.out.println("Money conserved (no lost updates): " + (totalBefore == totalAfter));
        System.out.println("Completed without deadlock; transfers logged: " + transfersLogged.get());
    }
}

class Account {
    private int balance;

    public Account(int initialBalance) {
        this.balance = initialBalance;
    }

    public synchronized void deposit(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount must be positive");
        balance += amount;
    }

    public synchronized void withdraw(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount must be positive");
        if (balance < amount) throw new IllegalArgumentException("Insufficient funds");
        balance -= amount;
    }

    public synchronized int getBalance() {
        return balance;
    }
}

// Stage 4: a log abstraction the Bank depends on. Because it is an interface
// injected via the constructor, tests can pass a mock/fake instead of the real one.
interface TransferLog {
    void record(String fromAccountId, String toAccountId, int amount);
}

// A simple real implementation. Tests would substitute a mock for this.
class ConsoleTransferLog implements TransferLog {
    @Override
    public void record(String fromAccountId, String toAccountId, int amount) {
        System.out.println("TRANSFER " + amount + " from " + fromAccountId + " to " + toAccountId);
    }
}

// STage 2 and 3 using concurrent hash map

class Bank {
    private final Map<String, Account> accounts = new java.util.concurrent.ConcurrentHashMap<>();
    private final TransferLog log; // Stage 4: injected dependency

    public Bank(TransferLog log) {
        this.log = log;
    }

    public void openAccount(String accountId, int initialBalance) {
        accounts.put(accountId, new Account(initialBalance));
    }

    public void transfer(String fromAccountId, String toAccountId, int amount) {
        if (fromAccountId.equals(toAccountId)) throw new IllegalArgumentException("Cannot transfer to the same account");
        Account fromAccount = accounts.get(fromAccountId);
        Account toAccount = accounts.get(toAccountId);
        if (fromAccount == null || toAccount == null) throw new IllegalArgumentException("Invalid account ID");

        // Stage 3 (fixed): order locks by the unique account IDs, not by sender/receiver,
        // so two opposite transfers (X->Y and Y->X) always grab locks in the same order.
        Account firstLock  = fromAccountId.compareTo(toAccountId) < 0 ? fromAccount : toAccount;
        Account secondLock = (firstLock == fromAccount) ? toAccount : fromAccount;

        synchronized (firstLock) {
            synchronized (secondLock) {
                fromAccount.withdraw(amount);
                toAccount.deposit(amount);
            }
        }

        // Stage 4: record only after the transfer succeeds, outside the locks
        // (so a slow/blocking log can't hold account locks).
        log.record(fromAccountId, toAccountId, amount);
    }

    public int getBalance(String accountId) {
        Account account = accounts.get(accountId);
        if (account == null) throw new IllegalArgumentException("Invalid account ID");
        return account.getBalance();
    }
}