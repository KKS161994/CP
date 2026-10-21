import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

/**
 * JUnit 5 + Mockito tests for BankLedger, covering all four stages.
 * Stage 4 is where Mockito earns its keep: the injected {@link TransferLog}
 * is a mock whose interactions we verify.
 */
class BankLedgerTest {

    // ---------- Stage 1: Account ----------

    @Test
    void depositIncreasesBalance() {
        Account a = new Account(100);
        a.deposit(50);
        assertEquals(150, a.getBalance());
    }

    @Test
    void withdrawDecreasesBalance() {
        Account a = new Account(100);
        a.withdraw(40);
        assertEquals(60, a.getBalance());
    }

    @Test
    void withdrawRejectsInsufficientFunds() {
        Account a = new Account(30);
        assertThrows(IllegalArgumentException.class, () -> a.withdraw(100));
        assertEquals(30, a.getBalance(), "balance must be untouched after a failed withdraw");
    }

    @Test
    void rejectsNegativeAmounts() {
        Account a = new Account(100);
        assertThrows(IllegalArgumentException.class, () -> a.deposit(-1));
        assertThrows(IllegalArgumentException.class, () -> a.withdraw(-1));
    }

    // ---------- Stage 2: Bank ----------

    @Test
    void transferMovesMoney() {
        Bank bank = new Bank(mock(TransferLog.class));
        bank.openAccount("A", 100);
        bank.openAccount("B", 0);
        bank.transfer("A", "B", 30);
        assertEquals(70, bank.getBalance("A"));
        assertEquals(30, bank.getBalance("B"));
    }

    @Test
    void rejectsSameAccountTransfer() {
        Bank bank = new Bank(mock(TransferLog.class));
        bank.openAccount("A", 100);
        assertThrows(IllegalArgumentException.class, () -> bank.transfer("A", "A", 10));
    }

    @Test
    void rejectsUnknownAccount() {
        Bank bank = new Bank(mock(TransferLog.class));
        bank.openAccount("A", 100);
        assertThrows(IllegalArgumentException.class, () -> bank.transfer("A", "ghost", 10));
        assertThrows(IllegalArgumentException.class, () -> bank.getBalance("ghost"));
    }

    // ---------- Stage 3: Concurrency ----------

    @Test
    void noLostUpdatesUnderConcurrency() throws Exception {
        Account a = new Account(0);
        int threads = 8;
        int perThread = 10_000;
        Thread[] pool = new Thread[threads];
        for (int t = 0; t < threads; t++) {
            pool[t] = new Thread(() -> {
                for (int i = 0; i < perThread; i++) a.deposit(1);
            });
        }
        for (Thread th : pool) th.start();
        for (Thread th : pool) th.join();
        assertEquals(threads * perThread, a.getBalance());
    }

    @Test
    void oppositeTransfersDoNotDeadlock() {
        // assertTimeoutPreemptively fails (instead of hanging forever) if the
        // workload deadlocks, which is exactly the Stage 3 regression we guard against.
        assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
            Bank bank = new Bank(mock(TransferLog.class));
            bank.openAccount("A", 100_000);
            bank.openAccount("B", 100_000);
            int threads = 8;
            int perThread = 2_000;
            CountDownLatch go = new CountDownLatch(1);
            Thread[] pool = new Thread[threads];
            for (int t = 0; t < threads; t++) {
                boolean aToB = (t % 2 == 0);
                pool[t] = new Thread(() -> {
                    try { go.await(); } catch (InterruptedException e) { return; }
                    for (int i = 0; i < perThread; i++) {
                        try {
                            if (aToB) bank.transfer("A", "B", 1);
                            else      bank.transfer("B", "A", 1);
                        } catch (IllegalArgumentException ignored) {
                            // insufficient funds is acceptable under contention
                        }
                    }
                });
                pool[t].start();
            }
            go.countDown();
            for (Thread th : pool) th.join();
            assertEquals(200_000, bank.getBalance("A") + bank.getBalance("B"),
                    "money must be conserved under concurrency");
        });
    }

    // ---------- Stage 4: Injected log (Mockito) ----------

    @Test
    void logsEachSuccessfulTransfer() {
        TransferLog log = mock(TransferLog.class);
        Bank bank = new Bank(log);
        bank.openAccount("A", 100);
        bank.openAccount("B", 0);

        bank.transfer("A", "B", 30);
        bank.transfer("A", "B", 20);

        verify(log, times(2)).record(anyString(), anyString(), anyInt());
        verify(log).record("A", "B", 30);
        verify(log).record("A", "B", 20);
    }

    @Test
    void logsExactTransferArgumentsViaCaptor() {
        TransferLog log = mock(TransferLog.class);
        Bank bank = new Bank(log);
        bank.openAccount("A", 100);
        bank.openAccount("B", 0);

        bank.transfer("A", "B", 42);

        ArgumentCaptor<Integer> amount = ArgumentCaptor.forClass(Integer.class);
        verify(log).record(eq("A"), eq("B"), amount.capture());
        assertEquals(42, amount.getValue());
    }

    @Test
    void doesNotLogFailedTransfer() {
        TransferLog log = mock(TransferLog.class);
        Bank bank = new Bank(log);
        bank.openAccount("A", 10);
        bank.openAccount("B", 0);

        assertThrows(IllegalArgumentException.class, () -> bank.transfer("A", "B", 9999));

        // A transfer that throws must never reach the log.
        verify(log, never()).record(anyString(), anyString(), anyInt());
    }

    @Test
    void doesNotLogRejectedSameAccountTransfer() {
        TransferLog log = mock(TransferLog.class);
        Bank bank = new Bank(log);
        bank.openAccount("A", 100);

        assertThrows(IllegalArgumentException.class, () -> bank.transfer("A", "A", 10));

        verifyNoInteractions(log);
    }
}
