/**
 * Create an account ledger that can be used to transfer money between accounts.
 *
 * The function will have high concurrency
 * Also the transfer function will be called by multiple threads at the same time.
 *
 * Approach: each Account carries its own Semaphore(1), so two unrelated transfers
 * (A<->B and C<->D) proceed in parallel. A transfer acquires the TWO involved
 * accounts' permits in a consistent ID order, so opposite transfers (A->B and
 * B->A) can never deadlock.
 */

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

class SemaphoreExampleV2 {

    static class Account {
        final String id;
        final Semaphore lock = new Semaphore(1); // per-account mutex
        int balance;

        Account(String id, int balance) {
            this.id = id;
            this.balance = balance;
        }
    }

    static class Ledger {
        private final Map<String, Account> accounts = new ConcurrentHashMap<>();

        void openAccount(String id, int initialBalance) {
            if (initialBalance < 0) throw new IllegalArgumentException("Initial balance must be non-negative");
            accounts.put(id, new Account(id, initialBalance));
        }

        void transfer(String fromId, String toId, int amount) {
            if (fromId.equals(toId)) throw new IllegalArgumentException("Cannot transfer to the same account");
            if (amount < 0) throw new IllegalArgumentException("Amount must be positive");
            Account from = accounts.get(fromId);
            Account to = accounts.get(toId);
            if (from == null || to == null) throw new IllegalArgumentException("Invalid account ID");

            // Acquire permits in a consistent global order (by id) to avoid deadlock,
            // NOT by sender/receiver role.
            Account firstLock  = fromId.compareTo(toId) < 0 ? from : to;
            Account secondLock = (firstLock == from) ? to : from;

            firstLock.lock.acquireUninterruptibly();
            try {
                secondLock.lock.acquireUninterruptibly();
                try {
                    if (from.balance < amount) throw new IllegalArgumentException("Insufficient funds");
                    from.balance -= amount;
                    to.balance += amount;
                } finally {
                    secondLock.lock.release();
                }
            } finally {
                firstLock.lock.release();
            }
        }

        int getBalance(String id) {
            Account a = accounts.get(id);
            if (a == null) throw new IllegalArgumentException("Invalid account ID");
            a.lock.acquireUninterruptibly();
            try {
                return a.balance;
            } finally {
                a.lock.release();
            }
        }

        int totalBalance() {
            int total = 0;
            for (Account a : accounts.values()) {
                a.lock.acquireUninterruptibly();
                try {
                    total += a.balance;
                } finally {
                    a.lock.release();
                }
            }
            return total;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Ledger ledger = new Ledger();
        String[] ids = {"A", "B", "C", "D"};
        for (String id : ids) ledger.openAccount(id, 1000);
        int totalBefore = ledger.totalBalance(); // 4000

        int threadCount = 8;
        int transfersPerThread = 5_000;
        Thread[] pool = new Thread[threadCount];
        for (int t = 0; t < threadCount; t++) {
            final int seed = t;
            pool[t] = new Thread(() -> {
                for (int i = 0; i < transfersPerThread; i++) {
                    // Cycle through pairs so opposite-direction transfers overlap.
                    String from = ids[(seed + i) % ids.length];
                    String to   = ids[(seed + i + 1) % ids.length];
                    try {
                        ledger.transfer(from, to, 1);
                    } catch (IllegalArgumentException e) {
                        // Insufficient funds can happen under contention; ignore.
                    }
                }
            });
            pool[t].start();
        }
        for (Thread th : pool) th.join();

        int totalAfter = ledger.totalBalance();
        System.out.println("Balances: A=" + ledger.getBalance("A") + " B=" + ledger.getBalance("B")
                + " C=" + ledger.getBalance("C") + " D=" + ledger.getBalance("D"));
        System.out.println("Total before: " + totalBefore + ", after: " + totalAfter);
        System.out.println("Money conserved (no lost updates): " + (totalBefore == totalAfter));
        System.out.println("Completed without deadlock after "
                + (threadCount * transfersPerThread) + " concurrent transfers.");
    }
}
