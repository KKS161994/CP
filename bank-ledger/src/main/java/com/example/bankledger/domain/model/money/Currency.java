package com.example.bankledger.domain.model.money;

/**
 * A small, explicit set of supported currencies. Kept as an enum (rather than
 * java.util.Currency) so the domain owns exactly what it supports and how many
 * fraction digits each currency carries.
 */
public enum Currency {

    USD("USD", 2),
    EUR("EUR", 2),
    GBP("GBP", 2),
    INR("INR", 2),
    JPY("JPY", 0);

    private final String code;
    private final int defaultFractionDigits;

    Currency(String code, int defaultFractionDigits) {
        this.code = code;
        this.defaultFractionDigits = defaultFractionDigits;
    }

    public String code() {
        return code;
    }

    public int defaultFractionDigits() {
        return defaultFractionDigits;
    }
}
