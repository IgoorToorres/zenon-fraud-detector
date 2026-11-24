package br.com.zenon.fraud;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FraudAnalyzerTest {

    private final FraudAnalyzer analyzer = new FraudAnalyzer();

    @Test
    void shouldKeepOnlyFraudulentTransactions() {
        Transaction fraud = transaction(PaymentType.TRANSFER, "100.00", "C1", true);
        Transaction legitimate = transaction(PaymentType.PAYMENT, "50.00", "C2", false);

        List<Transaction> result = analyzer.findFrauds(List.of(fraud, legitimate));

        assertEquals(List.of(fraud), result);
    }

    @Test
    void shouldReturnThreeFraudsWithHighestAmounts() {
        Transaction first = transaction(PaymentType.TRANSFER, "100.00", "C1", true);
        Transaction second = transaction(PaymentType.CASH_OUT, "90.00", "C2", true);
        Transaction third = transaction(PaymentType.TRANSFER, "80.00", "C3", true);
        Transaction fourth = transaction(PaymentType.CASH_OUT, "70.00", "C4", true);

        List<Transaction> result = analyzer.findTopThreeFrauds(
                List.of(third, fourth, first, second)
        );

        assertEquals(List.of(first, second, third), result);
    }

    @Test
    void shouldReturnFiveDistinctCustomersOrderedByHighestFraud() {
        List<Transaction> frauds = List.of(
                transaction(PaymentType.TRANSFER, "90.00", "C1", true),
                transaction(PaymentType.TRANSFER, "100.00", "C1", true),
                transaction(PaymentType.CASH_OUT, "80.00", "C2", true),
                transaction(PaymentType.CASH_OUT, "70.00", "C3", true),
                transaction(PaymentType.TRANSFER, "60.00", "C4", true),
                transaction(PaymentType.TRANSFER, "50.00", "C5", true),
                transaction(PaymentType.CASH_OUT, "40.00", "C6", true)
        );

        List<String> result = analyzer.findTopFiveSuspiciousCustomers(frauds);

        assertEquals(List.of("C1", "C2", "C3", "C4", "C5"), result);
    }

    @Test
    void shouldSumFraudAmounts() {
        List<Transaction> frauds = List.of(
                transaction(PaymentType.TRANSFER, "10.00", "C1", true),
                transaction(PaymentType.CASH_OUT, "20.00", "C2", true),
                transaction(PaymentType.TRANSFER, "30.00", "C3", true)
        );

        BigDecimal result = analyzer.calculateTotalLoss(frauds);

        assertEquals(new BigDecimal("60.00"), result);
    }

    @Test
    void shouldCountFraudsByPaymentType() {
        List<Transaction> frauds = List.of(
                transaction(PaymentType.TRANSFER, "10.00", "C1", true),
                transaction(PaymentType.CASH_OUT, "20.00", "C2", true),
                transaction(PaymentType.TRANSFER, "30.00", "C3", true)
        );

        Map<PaymentType, Long> result = analyzer.countFraudsByType(frauds);

        assertEquals(2L, result.get(PaymentType.TRANSFER));
        assertEquals(1L, result.get(PaymentType.CASH_OUT));
        assertEquals(2, result.size());
    }

    private Transaction transaction(
            PaymentType type,
            String amount,
            String originName,
            boolean isFraud
    ) {
        return new Transaction(
                1,
                type,
                new BigDecimal(amount),
                new Customer(originName, BigDecimal.ZERO, BigDecimal.ZERO),
                new Customer("M1", BigDecimal.ZERO, BigDecimal.ZERO),
                isFraud,
                false
        );
    }
}
