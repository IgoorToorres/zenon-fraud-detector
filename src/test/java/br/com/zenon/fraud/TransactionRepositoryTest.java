package br.com.zenon.fraud;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionRepositoryTest {

    private final Transaction first = transaction("C1", "10.00");
    private final Transaction second = transaction("C2", "20.00");
    private final List<Transaction> transactions = List.of(first, second);

    @Test
    void listRepositoryShouldFindExistingTransaction() {
        TransactionRepository repository =
                new TransactionListRepository(transactions);

        assertEquals(first, repository.findByOriginName("C1").orElseThrow());
    }

    @Test
    void listRepositoryShouldReturnEmptyForUnknownCustomer() {
        TransactionRepository repository =
                new TransactionListRepository(transactions);

        assertTrue(repository.findByOriginName("C12345").isEmpty());
    }

    @Test
    void mapRepositoryShouldFindExistingTransaction() {
        TransactionRepository repository =
                new TransactionMapRepository(transactions);

        assertEquals(second, repository.findByOriginName("C2").orElseThrow());
    }

    @Test
    void mapRepositoryShouldReturnEmptyForUnknownCustomer() {
        TransactionRepository repository =
                new TransactionMapRepository(transactions);

        assertTrue(repository.findByOriginName("C12345").isEmpty());
    }

    private Transaction transaction(String originName, String amount) {
        return new Transaction(
                1,
                PaymentType.PAYMENT,
                new BigDecimal(amount),
                new Customer(originName, BigDecimal.ZERO, BigDecimal.ZERO),
                new Customer("M1", BigDecimal.ZERO, BigDecimal.ZERO),
                false,
                false
        );
    }
}
