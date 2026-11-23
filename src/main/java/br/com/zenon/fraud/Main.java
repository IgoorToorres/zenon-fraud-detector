package br.com.zenon.fraud;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String fileName =
                "data/paysim_with_bad_data.csv";

        TransactionIngestor ingestor = new TransactionIngestor();

        List<Transaction> transactions =
                ingestor.ingest(fileName);

        System.out.println(transactions.size());
        transactions.forEach(System.out::println);
    }
}
