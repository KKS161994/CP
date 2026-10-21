package com.example.bankledger.domain;

import com.example.bankledger.domain.event.AccountOpened;
import com.example.bankledger.domain.event.MoneyDeposited;
import com.example.bankledger.domain.exception.AccountClosedException;
import com.example.bankledger.domain.exception.AccountNotEmptyException;
import com.example.bankledger.domain.exception.InsufficientFundsException;
import com.example.bankledger.domain.model.account.Account;
import com.example.bankledger.domain.model.account.AccountHolder;
import com.example.bankledger.domain.model.account.AccountId;
import com.example.bankledger.domain.model.account.AccountStatus;
import com.example.bankledger.domain.model.ledger.EntryType;
import com.example.bankledger.domain.model.money.Currency;
import com.example.bankledger.domain.model.money.Money;
import com.example.bankledger.domain.shared.DomainEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountTest {

    private Account newAccount() {
        return Account.open(AccountId.generate(), new AccountHolder("Alice"), Currency.USD);
    }

    @Test
    void opensWithZeroBalanceAndRaisesEvent() {
        Account account = newAccount();
        assertEquals(Money.zero(Currency.USD), account.balance());
        assertEquals(AccountStatus.OPEN, account.status());

        List<DomainEvent> events = account.pullDomainEvents();
        assertEquals(1, events.size());
        assertInstanceOf(AccountOpened.class, events.get(0));
    }

    @Test
    void depositIncreasesBalanceAndPostsCreditEntry() {
        Account account = newAccount();
        account.pullDomainEvents(); // discard the open event

        account.deposit(Money.of("100.00", Currency.USD), "Paycheck");

        assertEquals(Money.of("100.00", Currency.USD), account.balance());
        assertEquals(1, account.ledger().size());
        assertEquals(EntryType.CREDIT, account.ledger().get(0).type());
        assertInstanceOf(MoneyDeposited.class, account.pullDomainEvents().get(0));
    }

    @Test
    void withdrawDecreasesBalanceAndPostsDebitEntry() {
        Account account = newAccount();
        account.deposit(Money.of("100.00", Currency.USD), "Paycheck");

        account.withdraw(Money.of("40.00", Currency.USD), "Groceries");

        assertEquals(Money.of("60.00", Currency.USD), account.balance());
        assertEquals(EntryType.DEBIT, account.ledger().get(1).type());
    }

    @Test
    void cannotOverdraw() {
        Account account = newAccount();
        account.deposit(Money.of("30.00", Currency.USD), "Paycheck");

        assertThrows(InsufficientFundsException.class,
                () -> account.withdraw(Money.of("31.00", Currency.USD), "Too much"));
        // Balance is untouched after a rejected withdrawal.
        assertEquals(Money.of("30.00", Currency.USD), account.balance());
    }

    @Test
    void rejectsNonPositiveAmounts() {
        Account account = newAccount();
        assertThrows(IllegalArgumentException.class,
                () -> account.deposit(Money.zero(Currency.USD), "nothing"));
    }

    @Test
    void closedAccountRejectsMovements() {
        Account account = newAccount();
        account.close();

        assertEquals(AccountStatus.CLOSED, account.status());
        assertThrows(AccountClosedException.class,
                () -> account.deposit(Money.of("1.00", Currency.USD), "after close"));
    }

    @Test
    void cannotCloseAccountWithBalance() {
        Account account = newAccount();
        account.deposit(Money.of("5.00", Currency.USD), "Paycheck");

        assertThrows(AccountNotEmptyException.class, account::close);
    }

    @Test
    void balanceEqualsLastEntryBalanceAfter() {
        Account account = newAccount();
        account.deposit(Money.of("100.00", Currency.USD), "in");
        account.withdraw(Money.of("25.00", Currency.USD), "out");
        account.deposit(Money.of("10.00", Currency.USD), "in");

        Money last = account.ledger().get(account.ledger().size() - 1).balanceAfter();
        assertEquals(account.balance(), last);
        assertTrue(account.balance().isPositive());
    }
}
