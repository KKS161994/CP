# Bank Ledger (DDD)

A small **double-entry bank ledger** modeled with **Domain-Driven Design** in plain Java
(no framework). It demonstrates the classic DDD tactical patterns — aggregates, value
objects, domain events, repositories, and domain/application services — with a clean
layered (hexagonal) architecture and an in-memory persistence adapter.

## Requirements

- JDK 17+
- Maven 3.8+

## Build & test

```bash
mvn test        # compile and run the JUnit 5 suite
mvn compile exec:java -Dexec.mainClass=com.example.bankledger.BankLedgerDemo   # run the demo
```

(Or after `mvn compile`: `java -cp target/classes com.example.bankledger.BankLedgerDemo`.)

## Architecture

The dependency rule points inward: **infrastructure → application → domain**. The domain
depends on nothing outside itself.

```
com.example.bankledger
├── domain                     # the model — no framework, no I/O
│   ├── model
│   │   ├── money              # Money, Currency  (value objects)
│   │   ├── account            # Account (aggregate root), AccountId, AccountHolder, AccountStatus
│   │   └── ledger             # LedgerEntry (local entity), EntryId, EntryType
│   ├── event                  # AccountOpened, MoneyDeposited, MoneyWithdrawn, AccountClosed
│   ├── exception              # InsufficientFundsException, AccountClosedException, ...
│   ├── repository             # AccountRepository  (port)
│   ├── service                # TransferService  (domain service)
│   └── shared                 # DomainEvent, DomainEventPublisher
├── application                # use-case orchestration (thin)
│   ├── command                # OpenAccountCommand, DepositCommand, WithdrawCommand, TransferCommand
│   ├── view                   # AccountView, LedgerEntryView  (read-model DTOs)
│   └── AccountApplicationService
├── infrastructure             # adapters that implement the ports
│   ├── persistence            # InMemoryAccountRepository
│   └── event                  # LoggingDomainEventPublisher
└── BankLedgerDemo             # composition root
```

## Key design decisions

- **`Account` is the aggregate root and the consistency boundary.** All money movements
  go through it, so its invariants always hold: the balance never goes negative, it
  equals the sum of the ledger, every movement is an immutable `LedgerEntry`, a closed
  account rejects movements, and all amounts share the account's currency.
- **`Money` is a self-validating value object** over `BigDecimal`. Arithmetic across
  currencies throws; scale is normalized so `10.5 USD == 10.50 USD`.
- **Ledger entries are append-only.** Corrections are made by posting compensating
  entries, mirroring how a real ledger works.
- **Domain events** are raised by the aggregate and pulled by the application service
  after saving, then handed to a `DomainEventPublisher` port.
- **Repository is a port** declared in the domain; the in-memory adapter lives in
  infrastructure and hands out independent snapshots so unsaved changes never leak.
- **`TransferService`** is a domain service for the one operation that spans two
  aggregates; it debits the source first so an overdraft aborts before any credit.

## Extending it

- Swap `InMemoryAccountRepository` for a JPA/JDBC adapter — nothing in the domain changes.
- Replace `LoggingDomainEventPublisher` with an outbox or message-broker adapter.
- Add a REST or CLI layer that calls `AccountApplicationService`.
```
