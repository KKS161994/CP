package com.example.bankledger.application;

import com.example.bankledger.application.command.DepositCommand;
import com.example.bankledger.application.command.OpenAccountCommand;
import com.example.bankledger.application.command.TransferCommand;
import com.example.bankledger.application.command.WithdrawCommand;
import com.example.bankledger.application.view.AccountView;
import com.example.bankledger.domain.exception.AccountNotFoundException;
import com.example.bankledger.domain.exception.InsufficientFundsException;
import com.example.bankledger.domain.model.account.AccountId;
import com.example.bankledger.domain.model.money.Currency;
import com.example.bankledger.domain.model.money.Money;
import com.example.bankledger.domain.repository.AccountRepository;
import com.example.bankledger.domain.service.TransferService;
import com.example.bankledger.domain.shared.DomainEvent;
import com.example.bankledger.domain.shared.DomainEventPublisher;
import com.example.bankledger.infrastructure.persistence.InMemoryAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountApplicationServiceTest {

    private AccountApplicationService service;
    private List<DomainEvent> publishedEvents;

    @BeforeEach
    void setUp() {
        AccountRepository repository = new InMemoryAccountRepository();
        publishedEvents = new ArrayList<>();
        DomainEventPublisher publisher = publishedEvents::addAll;
        service = new AccountApplicationService(repository, new TransferService(), publisher);
    }

    @Test
    void openDepositAndQuery() {
        AccountId id = service.openAccount(new OpenAccountCommand("Alice", Currency.USD));
        service.deposit(new DepositCommand(id, Money.of("500.00", Currency.USD), "Salary"));

        AccountView view = service.getAccount(id);
        assertEquals("500.00 USD", view.balance());
        assertEquals("Alice", view.holder());
        assertEquals(1, view.ledger().size());
    }

    @Test
    void transferMovesMoneyBetweenAccounts() {
        AccountId alice = service.openAccount(new OpenAccountCommand("Alice", Currency.USD));
        AccountId bob = service.openAccount(new OpenAccountCommand("Bob", Currency.USD));
        service.deposit(new DepositCommand(alice, Money.of("1000.00", Currency.USD), "Salary"));

        service.transfer(new TransferCommand(alice, bob, Money.of("300.00", Currency.USD), "Rent"));

        assertEquals("700.00 USD", service.getAccount(alice).balance());
        assertEquals("300.00 USD", service.getAccount(bob).balance());
    }

    @Test
    void failedTransferLeavesBothAccountsUnchanged() {
        AccountId alice = service.openAccount(new OpenAccountCommand("Alice", Currency.USD));
        AccountId bob = service.openAccount(new OpenAccountCommand("Bob", Currency.USD));
        service.deposit(new DepositCommand(alice, Money.of("100.00", Currency.USD), "Salary"));

        assertThrows(InsufficientFundsException.class, () ->
                service.transfer(new TransferCommand(alice, bob, Money.of("500.00", Currency.USD), "Rent")));

        // Nothing was saved because the debit failed before either account was persisted.
        assertEquals("100.00 USD", service.getAccount(alice).balance());
        assertEquals("0.00 USD", service.getAccount(bob).balance());
    }

    @Test
    void withdrawingFromUnknownAccountFails() {
        assertThrows(AccountNotFoundException.class, () ->
                service.withdraw(new WithdrawCommand(AccountId.generate(),
                        Money.of("1.00", Currency.USD), "nope")));
    }

    @Test
    void publishesDomainEventsAsUseCasesRun() {
        AccountId id = service.openAccount(new OpenAccountCommand("Alice", Currency.USD));
        service.deposit(new DepositCommand(id, Money.of("10.00", Currency.USD), "Salary"));

        // One AccountOpened + one MoneyDeposited.
        assertTrue(publishedEvents.size() >= 2);
    }
}
