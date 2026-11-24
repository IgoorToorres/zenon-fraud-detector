package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FraudAnalyzer {

    public List<Transaction> findFrauds(List<Transaction> transactions) {
        return transactions.stream()
                .filter(Transaction::isFraud)
                .toList();
    }

    public List<Transaction> findTopThreeFrauds(List<Transaction> frauds) {
        return frauds.stream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .limit(3)
                .toList();
    }

    public List<String> findTopFiveSuspiciousCustomers(List<Transaction> frauds) {
        return frauds.stream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .map(transaction -> transaction.origin().name())
                .distinct()
                .limit(5)
                .toList();
    }

    public BigDecimal calculateTotalLoss(List<Transaction> frauds) {
        return frauds.stream()
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<PaymentType, Long> countFraudsByType(List<Transaction> frauds) {
        return frauds.stream()
                .collect(Collectors.groupingBy(
                        Transaction::type,
                        () -> new EnumMap<>(PaymentType.class),
                        Collectors.counting()
                ));
    }
}
