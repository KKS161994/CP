package com.example.bankledger;

import com.example.bankledger.application.AccountApplicationService;
import com.example.bankledger.application.command.DepositCommand;
import com.example.bankledger.application.command.OpenAccountCommand;
import com.example.bankledger.application.command.TransferCommand;
import com.example.bankledger.application.command.WithdrawCommand;
import com.example.bankledger.application.view.AccountView;
import com.example.bankledger.application.view.LedgerEntryView;
import com.example.bankledger.domain.model.account.AccountId;
import com.example.bankledger.domain.model.money.Currency;
import com.example.bankledger.domain.model.money.Money;
import com.example.bankledger.domain.repository.AccountRepository;
import com.example.bankledger.domain.service.TransferService;
import com.example.bankledger.domain.shared.DomainEventPublisher;
import com.example.bankledger.infrastructure.event.LoggingDomainEventPublisher;
import com.example.bankledger.infrastructure.persistence.InMemoryAccountRepository;

/**
 * Composition root: wires the in-memory adapters into the application service and
 * runs a short scenario, printing each account's ledger at the end.
 */
public final class BankLedgerDemo {

    public static void main(String[] args) {
        // Wire the dependency graph by hand (no framework).
        AccountRepository repository = new InMemoryAccountRepository();
        TransferService transferService = new TransferService();
        DomainEventPublisher publisher = new LoggingDomainEventPublisher();
        AccountApplicationService service =
                new AccountApplicationService(repository, transferService, publisher);

        // Open two accounts.
        AccountId alice = service.openAccount(new OpenAccountCommand("Alice", Currency.USD));
        AccountId bob = service.openAccount(new OpenAccountCommand("Bob", Currency.USD));

        // Move some money around.
        service.deposit(new DepositCommand(alice, Money.of("1000.00", Currency.USD), "Initial deposit"));
        service.deposit(new DepositCommand(bob, Money.of("250.00", Currency.USD), "Initial deposit"));
        service.withdraw(new WithdrawCommand(alice, Money.of("120.50", Currency.USD), "ATM withdrawal"));
        service.transfer(new TransferCommand(alice, bob, Money.of("300.00", Currency.USD), "Rent share"));

        // Print the resulting ledgers.
        printLedger(service.getAccount(alice));
        printLedger(service.getAccount(bob));
    }

    private static void printLedger(AccountView account) {
        System.out.printf("%n=== %s (%s) — balance %s [%s] ===%n",
                account.holder(), account.id(), account.balance(), account.status());
        for (LedgerEntryView entry : account.ledger()) {
            System.out.printf("  %-6s %12s  balance=%-12s  %s%n",
                    entry.type(), entry.amount(), entry.balanceAfter(), entry.description());
        }
    }

    private BankLedgerDemo() {
    }
}
