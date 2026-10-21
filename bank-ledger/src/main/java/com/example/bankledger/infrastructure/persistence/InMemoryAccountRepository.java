package com.example.bankledger.infrastructure.persistence;

import com.example.bankledger.domain.model.account.Account;
import com.example.bankledger.domain.model.account.AccountId;
import com.example.bankledger.domain.repository.AccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An in-memory {@link AccountRepository} adapter, suitable for tests and demos.
 *
 * <p>To behave like a real store, each {@code save} captures an independent snapshot
 * of the aggregate and each {@code findById} reconstitutes a fresh instance. Callers
 * therefore never share a mutable aggregate with the repository, which would otherwise
 * let unsaved changes silently leak between use cases.
 */
public class InMemoryAccountRepository implements AccountRepository {

    private final Map<AccountId, Account> store = new ConcurrentHashMap<>();

    @Override
    public void save(Account account) {
        store.put(account.id(), snapshot(account));
    }

    @Override
    public Optional<Account> findById(AccountId id) {
        return Optional.ofNullable(store.get(id)).map(this::snapshot);
    }

    @Override
    public List<Account> findAll() {
        List<Account> all = new ArrayList<>();
        for (Account account : store.values()) {
            all.add(snapshot(account));
        }
        return all;
    }

    @Override
    public AccountId nextIdentity() {
        return AccountId.generate();
    }

    /**
     * Build an independent copy. Ledger entries are immutable so they can be shared;
     * only the aggregate shell is recreated.
     */
    private Account snapshot(Account account) {
        return Account.rehydrate(
                account.id(),
                account.holder(),
                account.currency(),
                account.status(),
                new ArrayList<>(account.ledger()));
    }
}
