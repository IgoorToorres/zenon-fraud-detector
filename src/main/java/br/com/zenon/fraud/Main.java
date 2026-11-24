package br.com.zenon.fraud;

import java.io.IOException;
import java.util.List;

public class Main {

    private static final int WARM_UP_REPETITIONS = 10;
    private static final int BENCHMARK_REPETITIONS = 100;

    public static void main(String[] args) throws IOException {
        String fileName =
                "data/PS_20174392719_1491204439457_log.csv";

        TransactionIngestor ingestor = new TransactionIngestor();
        List<Transaction> transactions =
                ingestor.ingest(fileName, 100_000);

        TransactionRepository listRepository =
                new TransactionListRepository(transactions);

        printSearchResult(listRepository, "C12345");
        printSearchResult(listRepository, "C1231006815");

        Transaction lastTransaction =
                transactions.get(transactions.size() - 1);
        String lastOriginName = lastTransaction.origin().name();

        TransactionRepository mapRepository =
                new TransactionMapRepository(transactions);

        warmUp(listRepository, lastOriginName);
        warmUp(mapRepository, lastOriginName);

        long listTime = measureAverageSearchTime(
                listRepository,
                lastOriginName
        );
        long mapTime = measureAverageSearchTime(
                mapRepository,
                lastOriginName
        );

        System.out.println("Cliente usado no pior caso: " + lastOriginName);
        System.out.println("Tempo médio da busca na List: " + listTime + " ns");
        System.out.println("Tempo médio da busca no Map: " + mapTime + " ns");
    }

    private static void printSearchResult(
            TransactionRepository repository,
            String originName
    ) {
        repository.findByOriginName(originName)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println(
                                "Transação não encontrada para o cliente " + originName
                        )
                );
    }

    private static void warmUp(
            TransactionRepository repository,
            String originName
    ) {
        for (int repetition = 0;
             repetition < WARM_UP_REPETITIONS;
             repetition++) {
            repository.findByOriginName(originName).orElseThrow();
        }
    }

    private static long measureAverageSearchTime(
            TransactionRepository repository,
            String originName
    ) {
        long start = System.nanoTime();

        for (int repetition = 0;
             repetition < BENCHMARK_REPETITIONS;
             repetition++) {
            repository.findByOriginName(originName).orElseThrow();
        }

        long elapsed = System.nanoTime() - start;
        return elapsed / BENCHMARK_REPETITIONS;
    }
}
