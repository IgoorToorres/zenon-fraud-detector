package br.com.zenon.fraud;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String fileName =
                "data/PS_20174392719_1491204439457_log.csv";

        TransactionIngestor ingestor = new TransactionIngestor();
        FraudAnalyzer analyzer = new FraudAnalyzer();

        List<Transaction> transactions =
                ingestor.ingest(fileName, 50_000);

        List<Transaction> frauds = analyzer.findFrauds(transactions);

        System.out.println("1. Total de Fraudes: " + frauds.size());

        System.out.println("2. Top 3 Fraudes de Maior Valor:");
        analyzer.findTopThreeFrauds(frauds)
                .forEach(transaction ->
                        System.out.println(formatMoney(transaction.amount()))
                );

        System.out.println("3. Clientes Suspeitos:");
        analyzer.findTopFiveSuspiciousCustomers(frauds)
                .forEach(System.out::println);

        System.out.println(
                "4. Prejuízo Total: "
                        + formatMoney(analyzer.calculateTotalLoss(frauds))
        );

        System.out.println("5. Fraudes por Tipo:");
        analyzer.countFraudsByType(frauds)
                .forEach((type, count) ->
                        System.out.println(" - " + type + ": " + count)
                );
    }

    private static String formatMoney(BigDecimal value) {
        return value.setScale(2).toPlainString();
    }
}
