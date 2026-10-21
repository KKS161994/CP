package com.example.bankledger.domain;

import com.example.bankledger.domain.model.money.Currency;
import com.example.bankledger.domain.model.money.CurrencyMismatchException;
import com.example.bankledger.domain.model.money.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoneyTest {

    @Test
    void addsAmountsOfTheSameCurrency() {
        Money result = Money.of("10.50", Currency.USD).plus(Money.of("4.50", Currency.USD));
        assertEquals(Money.of("15.00", Currency.USD), result);
    }

    @Test
    void treatsDifferentScalesOfTheSameAmountAsEqual() {
        assertEquals(Money.of("10.5", Currency.USD), Money.of("10.50", Currency.USD));
    }

    @Test
    void rejectsArithmeticAcrossCurrencies() {
        assertThrows(CurrencyMismatchException.class,
                () -> Money.of("10.00", Currency.USD).plus(Money.of("10.00", Currency.EUR)));
    }

    @Test
    void comparesAmounts() {
        assertTrue(Money.of("10.00", Currency.USD).isGreaterThan(Money.of("9.99", Currency.USD)));
        assertFalse(Money.of("10.00", Currency.USD).isLessThan(Money.of("9.99", Currency.USD)));
        assertTrue(Money.zero(Currency.USD).isZero());
    }
}
