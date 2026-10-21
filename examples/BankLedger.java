import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// =====================================================================================
// BankLedger — redesigned from a Domain-Driven Design perspective.
//
// Layering (outer depends on inner, never the reverse):
//   Application : Ledger                (facade; translates primitives -> domain types)
//   Domain      : Account (aggregate root), Money / AccountId (value objects),
//                 MoneyTransferService (domain service), MoneyTransferred (domain event),
//                 domain exceptions, AccountRepository (port)
//   Infrastructure : InMemoryAccountRepository, LoggingEventPublisher (adapters)
// =====================================================================================

// ---- Entry point / demo (kept first so `java BankLedger.java` runs it) ----
class BankLedgerDemo {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger transfersPublished = new AtomicInteger();
        DomainEventPublisher publisher = event -> transfersPublished.incrementAndGet();

        Ledger ledger = new Ledger(new InMemoryAccountRepository(), publisher);
        ledger.openAccount("A", 1000);
        ledger.openAccount("B", 1000);
        long totalBefore = ledger.balanceOf("A") + ledger.balanceOf("B");

        int threadCount = 8;
        int transfersPerThread = 1000;
        Thread[] pool = new Thread[threadCount];
        for (int t = 0; t < threadCount; t++) {
            boolean aToB = (t % 2 == 0);
            pool[t] = new Thread(() -> {
                for (int i = 0; i < transfersPerThread; i++) {
                    try {
                        if (aToB) ledger.transfer("A", "B", 1);
                        else      ledger.transfer("B", "A", 1);
                    } catch (InsufficientFundsException e) {
                        // legitimate under contention; ignore for the demo
                    }
                }
            });
        }
        for (Thread th : pool) th.start();
        for (Thread th : pool) th.join();

        long totalAfter = ledger.balanceOf("A") + ledger.balanceOf("B");
        System.out.println("Total before: " + totalBefore + ", after: " + totalAfter);
        System.out.println("Money conserved (no lost updates): " + (totalBefore == totalAfter));
        System.out.println("Completed without deadlock; events published: " + transfersPublished.get());
    }
}

// =====================================================================================
// APPLICATION LAYER
// =====================================================================================

/**
 * Application service / facade. Speaks primitives at its boundary (an anti-corruption
 * point) and translates them into domain value objects before delegating to the domain.
 */
class Ledger {
    private final AccountRepository accounts;
    private final MoneyTransferService transferService;

    Ledger(AccountRepository accounts, DomainEventPublisher publisher) {
        this.accounts = accounts;
        this.transferService = new MoneyTransferService(accounts, publisher);
    }

    void openAccount(String id, long initialBalance) {
        AccountId accountId = AccountId.of(id);
        accounts.save(new Account(accountId, Money.of(initialBalance)));
    }

    void transfer(String fromId, String toId, long amount) {
        transferService.transfer(AccountId.of(fromId), AccountId.of(toId), Money.of(amount));
    }

    long balanceOf(String id) {
        AccountId accountId = AccountId.of(id);
        return accounts.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId))
                .balance().minorUnits();
    }
}

// =====================================================================================
// DOMAIN LAYER — value objects
// =====================================================================================

/** Value object: a typed account identifier (no more bare Strings flowing around). */
record AccountId(String value) {
    AccountId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("AccountId must not be blank");
        }
    }
    static AccountId of(String value) { return new AccountId(value); }
}

/**
 * Value object: an immutable, non-negative amount of money (in minor units, e.g. cents).
 * All arithmetic returns a new instance; the type itself guarantees money is never negative.
 */
record Money(long minorUnits) {
    Money {
        if (minorUnits < 0) {
            throw new IllegalArgumentException("Money cannot be negative: " + minorUnits);
        }
    }
    static Money of(long minorUnits) { return new Money(minorUnits); }
    Money plus(Money other)  { return new Money(this.minorUnits + other.minorUnits); }
    Money minus(Money other) { return new Money(this.minorUnits - other.minorUnits); }
    boolean isLessThan(Money other) { return this.minorUnits < other.minorUnits; }
}

// =====================================================================================
// DOMAIN LAYER — aggregate root
// =====================================================================================

/**
 * Aggregate root. Has identity (AccountId) and protects its own invariant
 * (balance never goes negative). Concurrency is NOT its concern — that is
 * coordinated by the domain service. It only knows the rules of money.
 */
final class Account {
    private final AccountId id;
    private Money balance;

    Account(AccountId id, Money initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    AccountId id() { return id; }
    Money balance() { return balance; }

    void deposit(Money amount) {
        this.balance = this.balance.plus(amount);
    }

    void withdraw(Money amount) {
        if (balance.isLessThan(amount)) {
            throw new InsufficientFundsException(id, balance, amount);
        }
        this.balance = this.balance.minus(amount);
    }
}

// =====================================================================================
// DOMAIN LAYER — domain service (the cross-aggregate operation)
// =====================================================================================

/**
 * Domain service. A money transfer spans TWO aggregates, so it does not belong on
 * either Account alone. It coordinates them, enforces consistency, and publishes a
 * domain event on success.
 */
class MoneyTransferService {
    private final AccountRepository accounts;
    private final DomainEventPublisher publisher;

    MoneyTransferService(AccountRepository accounts, DomainEventPublisher publisher) {
        this.accounts = accounts;
        this.publisher = publisher;
    }

    void transfer(AccountId fromId, AccountId toId, Money amount) {
        if (fromId.equals(toId)) {
            throw new SameAccountTransferException(fromId);
        }
        Account from = accounts.findById(fromId).orElseThrow(() -> new AccountNotFoundException(fromId));
        Account to   = accounts.findById(toId).orElseThrow(() -> new AccountNotFoundException(toId));

        // Acquire the two aggregates' monitors in a consistent (id-ordered) order,
        // so opposite transfers can never deadlock. Concurrency lives here, not in Account.
        Account first  = fromId.value().compareTo(toId.value()) < 0 ? from : to;
        Account second = (first == from) ? to : from;

        synchronized (first) {
            synchronized (second) {
                from.withdraw(amount);
                to.deposit(amount);
                accounts.save(from); // explicit persistence intent (no-op for in-memory)
                accounts.save(to);
            }
        }

        publisher.publish(new MoneyTransferred(fromId, toId, amount));
    }
}

// =====================================================================================
// DOMAIN LAYER — domain events
// =====================================================================================

/** Marker for things that happened in the domain. */
interface DomainEvent { }

/** A fact: money was transferred. Past tense, immutable — this is what really happened. */
record MoneyTransferred(AccountId from, AccountId to, Money amount) implements DomainEvent { }

/** Port for emitting domain events (replaces the old "TransferLog" mechanism). */
interface DomainEventPublisher {
    void publish(DomainEvent event);
}

// =====================================================================================
// DOMAIN LAYER — repository port
// =====================================================================================

/** Port: how the domain persists/retrieves aggregates, with no storage detail leaking in. */
interface AccountRepository {
    Optional<Account> findById(AccountId id);
    void save(Account account);
}

// =====================================================================================
// DOMAIN LAYER — exceptions (ubiquitous language)
// =====================================================================================

abstract class DomainException extends RuntimeException {
    DomainException(String message) { super(message); }
}

class AccountNotFoundException extends DomainException {
    AccountNotFoundException(AccountId id) { super("No such account: " + id.value()); }
}

class SameAccountTransferException extends DomainException {
    SameAccountTransferException(AccountId id) { super("Cannot transfer to the same account: " + id.value()); }
}

class InsufficientFundsException extends DomainException {
    InsufficientFundsException(AccountId id, Money balance, Money requested) {
        super("Account " + id.value() + " has " + balance.minorUnits()
                + " but " + requested.minorUnits() + " was requested");
    }
}

// =====================================================================================
// INFRASTRUCTURE LAYER — adapters
// =====================================================================================

/** Adapter: an in-memory implementation of the repository port. */
class InMemoryAccountRepository implements AccountRepository {
    private final Map<AccountId, Account> store = new ConcurrentHashMap<>();

    @Override public Optional<Account> findById(AccountId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override public void save(Account account) {
        store.put(account.id(), account);
    }
}

/** Adapter: prints domain events. Swap for a real audit log, message bus, etc. */
class LoggingEventPublisher implements DomainEventPublisher {
    @Override public void publish(DomainEvent event) {
        System.out.println("EVENT: " + event);
    }
}
