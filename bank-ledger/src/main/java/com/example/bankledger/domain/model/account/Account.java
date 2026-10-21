package com.example.bankledger.domain.model.account;

import com.example.bankledger.domain.event.AccountClosed;
import com.example.bankledger.domain.event.AccountOpened;
import com.example.bankledger.domain.event.MoneyDeposited;
import com.example.bankledger.domain.event.MoneyWithdrawn;
import com.example.bankledger.domain.exception.AccountClosedException;
import com.example.bankledger.domain.exception.AccountNotEmptyException;
import com.example.bankledger.domain.exception.InsufficientFundsException;
import com.example.bankledger.domain.model.ledger.EntryId;
import com.example.bankledger.domain.model.ledger.EntryType;
import com.example.bankledger.domain.model.ledger.LedgerEntry;
import com.example.bankledger.domain.model.money.Currency;
import com.example.bankledger.domain.model.money.Money;
import com.example.bankledger.domain.shared.DomainEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The Account aggregate root and the consistency boundary of this bounded context.
 *
 * <p>All money movements go through this class so that its invariants always hold:
 * <ul>
 *   <li>the balance equals the sum of its ledger entries and is never negative;</li>
 *   <li>every movement is recorded as an immutable {@link LedgerEntry};</li>
 *   <li>a closed account accepts no further movements;</li>
 *   <li>all amounts share the account's {@link Currency}.</li>
 * </ul>
 *
 * <p>State changes raise {@link DomainEvent}s, which callers collect with
 * {@link #pullDomainEvents()} after persisting the aggregate.
 */
public class Account {

    private final AccountId id;
    private final AccountHolder holder;
    private final Currency currency;
    private AccountStatus status;
    private Money balance;
    private final List<LedgerEntry> entries = new ArrayList<>();
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private Account(AccountId id, AccountHolder holder, Currency currency) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.holder = Objects.requireNonNull(holder, "holder must not be null");
        this.currency = Objects.requireNonNull(currency, "currency must not be null");
        this.status = AccountStatus.OPEN;
        this.balance = Money.zero(currency);
    }

    /** Factory: open a brand-new account with a zero balance. */
    public static Account open(AccountId id, AccountHolder holder, Currency currency) {
        Account account = new Account(id, holder, currency);
        account.record(new AccountOpened(id, holder, currency, Instant.now()));
        return account;
    }

    /**
     * Reconstitute an existing account from persisted state without raising events.
     * Used by repositories when loading an aggregate.
     */
    public static Account rehydrate(AccountId id,
                                    AccountHolder holder,
                                    Currency currency,
                                    AccountStatus status,
                                    List<LedgerEntry> history) {
        Account account = new Account(id, holder, currency);
        account.status = status;
        account.entries.addAll(history);
        account.balance = history.isEmpty()
                ? Money.zero(currency)
                : history.get(history.size() - 1).balanceAfter();
        return account;
    }

    public void deposit(Money amount, String description) {
        ensureOpen();
        requirePositive(amount);
        requireSameCurrency(amount);

        balance = balance.plus(amount);
        post(EntryType.CREDIT, amount, description);
        record(new MoneyDeposited(id, amount, balance, Instant.now()));
    }

    public void withdraw(Money amount, String description) {
        ensureOpen();
        requirePositive(amount);
        requireSameCurrency(amount);
        if (amount.isGreaterThan(balance)) {
            throw new InsufficientFundsException(id, balance, amount);
        }

        balance = balance.minus(amount);
        post(EntryType.DEBIT, amount, description);
        record(new MoneyWithdrawn(id, amount, balance, Instant.now()));
    }

    /** Close the account. Only permitted once the balance has been fully drained. */
    public void close() {
        if (status == AccountStatus.CLOSED) {
            return;
        }
        if (!balance.isZero()) {
            throw new AccountNotEmptyException(id, balance);
        }
        status = AccountStatus.CLOSED;
        record(new AccountClosed(id, Instant.now()));
    }

    private void post(EntryType type, Money amount, String description) {
        entries.add(new LedgerEntry(
                EntryId.generate(), type, amount, balance, description, Instant.now()));
    }

    private void ensureOpen() {
        if (status == AccountStatus.CLOSED) {
            throw new AccountClosedException(id);
        }
    }

    private void requirePositive(Money amount) {
        Objects.requireNonNull(amount, "amount must not be null");
        if (!amount.isPositive()) {
            throw new IllegalArgumentException("amount must be positive, was " + amount);
        }
    }

    private void requireSameCurrency(Money amount) {
        if (amount.currency() != currency) {
            throw new com.example.bankledger.domain.model.money.CurrencyMismatchException(
                    currency, amount.currency());
        }
    }

    private void record(DomainEvent event) {
        domainEvents.add(event);
    }

    /** Return and clear the events accumulated since the last pull. */
    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> pulled = List.copyOf(domainEvents);
        domainEvents.clear();
        return pulled;
    }

    public AccountId id() {
        return id;
    }

    public AccountHolder holder() {
        return holder;
    }

    public Currency currency() {
        return currency;
    }

    public AccountStatus status() {
        return status;
    }

    public Money balance() {
        return balance;
    }

    public List<LedgerEntry> ledger() {
        return Collections.unmodifiableList(entries);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Account account)) {
            return false;
        }
        return id.equals(account.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Account[%s, holder=%s, balance=%s, status=%s]"
                .formatted(id, holder.name(), balance, status);
    }
}
