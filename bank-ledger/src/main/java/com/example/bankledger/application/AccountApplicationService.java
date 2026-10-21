package com.example.bankledger.application;

import com.example.bankledger.application.command.DepositCommand;
import com.example.bankledger.application.command.OpenAccountCommand;
import com.example.bankledger.application.command.TransferCommand;
import com.example.bankledger.application.command.WithdrawCommand;
import com.example.bankledger.application.view.AccountView;
import com.example.bankledger.domain.exception.AccountNotFoundException;
import com.example.bankledger.domain.model.account.Account;
import com.example.bankledger.domain.model.account.AccountHolder;
import com.example.bankledger.domain.model.account.AccountId;
import com.example.bankledger.domain.repository.AccountRepository;
import com.example.bankledger.domain.service.TransferService;
import com.example.bankledger.domain.shared.DomainEvent;
import com.example.bankledger.domain.shared.DomainEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The application layer: a thin orchestration boundary that turns commands into use
 * cases. It loads aggregates, invokes domain behaviour, persists the result, and
 * publishes the resulting domain events. It holds no business rules itself — those
 * live in the domain model.
 *
 * <p>Each public method represents one use case and is the unit of consistency; in a
 * framework-backed app these would be wrapped in a transaction.
 */
public class AccountApplicationService {

    private final AccountRepository accounts;
    private final TransferService transferService;
    private final DomainEventPublisher eventPublisher;

    public AccountApplicationService(AccountRepository accounts,
                                     TransferService transferService,
                                     DomainEventPublisher eventPublisher) {
        this.accounts = Objects.requireNonNull(accounts);
        this.transferService = Objects.requireNonNull(transferService);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }

    public AccountId openAccount(OpenAccountCommand command) {
        AccountId id = accounts.nextIdentity();
        Account account = Account.open(id, new AccountHolder(command.holderName()), command.currency());
        accounts.save(account);
        publishEventsOf(account);
        return id;
    }

    public void deposit(DepositCommand command) {
        Account account = load(command.accountId());
        account.deposit(command.amount(), command.description());
        accounts.save(account);
        publishEventsOf(account);
    }

    public void withdraw(WithdrawCommand command) {
        Account account = load(command.accountId());
        account.withdraw(command.amount(), command.description());
        accounts.save(account);
        publishEventsOf(account);
    }

    public void transfer(TransferCommand command) {
        Account from = load(command.fromAccountId());
        Account to = load(command.toAccountId());

        transferService.transfer(from, to, command.amount(), command.reference());

        accounts.save(from);
        accounts.save(to);
        publishEventsOf(from, to);
    }

    public AccountView getAccount(AccountId id) {
        return AccountView.from(load(id));
    }

    public List<AccountView> listAccounts() {
        return accounts.findAll().stream().map(AccountView::from).toList();
    }

    private Account load(AccountId id) {
        return accounts.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    private void publishEventsOf(Account... aggregates) {
        List<DomainEvent> events = new ArrayList<>();
        for (Account aggregate : aggregates) {
            events.addAll(aggregate.pullDomainEvents());
        }
        if (!events.isEmpty()) {
            eventPublisher.publish(events);
        }
    }
}
