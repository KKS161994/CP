package com.example.bankledger.domain.repository;

import com.example.bankledger.domain.model.account.Account;
import com.example.bankledger.domain.model.account.AccountId;

import java.util.List;
import java.util.Optional;

/**
 * Persistence port for the Account aggregate, expressed in domain terms. The
 * interface lives in the domain layer; concrete adapters (in-memory, JPA, …) live
 * in infrastructure. This keeps the domain free of any storage technology.
 */
public interface AccountRepository {

    void save(Account account);

    Optional<Account> findById(AccountId id);

    List<Account> findAll();

    AccountId nextIdentity();
}
