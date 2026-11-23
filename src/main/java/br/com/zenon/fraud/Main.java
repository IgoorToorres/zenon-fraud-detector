package br.com.zenon.fraud;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String fileName =
                "data/PS_20174392719_1491204439457_log.csv";

        TransactionIngestor ingestor = new TransactionIngestor();

        List<Transaction> transactions =
                ingestor.ingest(fileName);

        transactions.stream()
                .limit(10)
                .forEach(System.out::println);
    }
}