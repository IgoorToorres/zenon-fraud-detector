package br.com.zenon.fraud;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerTest {

    @Test
    void shouldRejectNullOrEmptyName() {
        IllegalArgumentException nullName = assertThrows(
                IllegalArgumentException.class,
                () -> new Customer(null, BigDecimal.ZERO, BigDecimal.ZERO)
        );
        IllegalArgumentException emptyName = assertThrows(
                IllegalArgumentException.class,
                () -> new Customer(" ", BigDecimal.ZERO, BigDecimal.ZERO)
        );

        assertEquals("name should not be null", nullName.getMessage());
        assertEquals("name should not be empty", emptyName.getMessage());
    }

    @Test
    void shouldRejectNullBalances() {
        IllegalArgumentException nullOldBalance = assertThrows(
                IllegalArgumentException.class,
                () -> new Customer("C1", null, BigDecimal.ZERO)
        );
        IllegalArgumentException nullNewBalance = assertThrows(
                IllegalArgumentException.class,
                () -> new Customer("C1", BigDecimal.ZERO, null)
        );

        assertEquals("oldBalance should not be null", nullOldBalance.getMessage());
        assertEquals("newBalance should not be null", nullNewBalance.getMessage());
    }

    @Test
    void shouldRejectNegativeBalances() {
        IllegalArgumentException negativeOldBalance = assertThrows(
                IllegalArgumentException.class,
                () -> new Customer("C1", new BigDecimal("-1.00"), BigDecimal.ZERO)
        );
        IllegalArgumentException negativeNewBalance = assertThrows(
                IllegalArgumentException.class,
                () -> new Customer("C1", BigDecimal.ZERO, new BigDecimal("-1.00"))
        );

        assertEquals("oldBalance should be positive: -1.00", negativeOldBalance.getMessage());
        assertEquals("newBalance should be positive: -1.00", negativeNewBalance.getMessage());
    }

    @Test
    void shouldAcceptZeroBalances() {
        assertDoesNotThrow(() -> new Customer("C1", BigDecimal.ZERO, BigDecimal.ZERO));
    }
}
