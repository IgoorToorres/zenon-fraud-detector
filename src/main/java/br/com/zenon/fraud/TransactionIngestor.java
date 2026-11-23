package br.com.zenon.fraud;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TransactionIngestor {

    private static final int MAX_TRANSACTIONS = 1_000;

    public List<Transaction> ingest(String fileName) throws IOException {
        List<Transaction> transactions =
                new ArrayList<>(MAX_TRANSACTIONS);

        Path filePath = Path.of(fileName);

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            reader.readLine(); // Ignora o cabeçalho do CSV

            String line;

            while (transactions.size() < MAX_TRANSACTIONS
                    && (line = reader.readLine()) != null) {

                Transaction transaction = parse(line);
                transactions.add(transaction);
            }
        }

        return transactions;
    }

    private Transaction parse(String line) {
        String[] columns = line.split(",", -1);

        if (columns.length != 11) {
            throw new IllegalArgumentException(
                    "Linha inválida: eram esperadas 11 colunas"
            );
        }

        Customer origin = new Customer(
                columns[3],
                new BigDecimal(columns[4]),
                new BigDecimal(columns[5])
        );

        Customer destination = new Customer(
                columns[6],
                new BigDecimal(columns[7]),
                new BigDecimal(columns[8])
        );

        return new Transaction(
                Integer.parseInt(columns[0]),
                PaymentType.valueOf(columns[1]),
                new BigDecimal(columns[2]),
                origin,
                destination,
                parseBoolean(columns[9]),
                parseBoolean(columns[10])
        );
    }

    private boolean parseBoolean(String value) {
        return switch (value) {
            case "0" -> false;
            case "1" -> true;
            default -> throw new IllegalArgumentException(
                    "Indicador booleano inválido: " + value
            );
        };
    }
}