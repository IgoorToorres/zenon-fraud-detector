package br.com.zenon.fraud;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionTest {

    private static final Customer ORIGIN =
            new Customer("C1", BigDecimal.TEN, BigDecimal.ZERO);
    private static final Customer DESTINATION =
            new Customer("C2", BigDecimal.ZERO, BigDecimal.TEN);

    @Test
    void shouldRejectStepLessThanOne() {
        IllegalArgumentException negativeStep = assertThrows(
                IllegalArgumentException.class,
                () -> transaction(-1, BigDecimal.TEN)
        );
        IllegalArgumentException zeroStep = assertThrows(
                IllegalArgumentException.class,
                () -> transaction(0, BigDecimal.TEN)
        );

        assertEquals("step should be positive: -1", negativeStep.getMessage());
        assertEquals("step should be positive: 0", zeroStep.getMessage());
    }

    @Test
    void shouldRejectNegativeAmount() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> transaction(1, new BigDecimal("-0.01"))
        );

        assertEquals("amount should be positive: -0.01", exception.getMessage());
    }

    @Test
    void shouldAcceptZeroAmount() {
        assertDoesNotThrow(() -> transaction(1, BigDecimal.ZERO));
    }

    @Test
    void shouldRejectNullReferenceValues() {
        assertEquals(
                "type should not be null",
                assertThrows(IllegalArgumentException.class, () ->
                        new Transaction(1, null, BigDecimal.ZERO, ORIGIN, DESTINATION, false, false)
                ).getMessage()
        );
        assertEquals(
                "amount should not be null",
                assertThrows(IllegalArgumentException.class, () ->
                        new Transaction(1, PaymentType.PAYMENT, null, ORIGIN, DESTINATION, false, false)
                ).getMessage()
        );
        assertEquals(
                "origin should not be null",
                assertThrows(IllegalArgumentException.class, () ->
                        new Transaction(1, PaymentType.PAYMENT, BigDecimal.ZERO, null, DESTINATION, false, false)
                ).getMessage()
        );
        assertEquals(
                "destination should not be null",
                assertThrows(IllegalArgumentException.class, () ->
                        new Transaction(1, PaymentType.PAYMENT, BigDecimal.ZERO, ORIGIN, null, false, false)
                ).getMessage()
        );
    }

    private Transaction transaction(int step, BigDecimal amount) {
        return new Transaction(
                step,
                PaymentType.PAYMENT,
                amount,
                ORIGIN,
                DESTINATION,
                false,
                false
        );
    }
}
