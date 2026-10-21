package com.example.bankledger.application.view;

import com.example.bankledger.domain.model.account.Account;

import java.util.List;

/**
 * A read-only projection of an {@link Account} and its ledger for callers of the
 * application service.
 */
public record AccountView(String id,
                          String holder,
                          String currency,
                          String status,
                          String balance,
                          List<LedgerEntryView> ledger) {

    public static AccountView from(Account account) {
        List<LedgerEntryView> ledger = account.ledger().stream()
                .map(LedgerEntryView::from)
                .toList();
        return new AccountView(
                account.id().toString(),
                account.holder().name(),
                account.currency().code(),
                account.status().name(),
                account.balance().toString(),
                ledger);
    }
}
