package br.com.zenon.fraud;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransactionMapRepository implements TransactionRepository {

    private final Map<String, Transaction> transactionsByOrigin = new HashMap<>();

    public TransactionMapRepository(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            transactionsByOrigin.putIfAbsent(
                    transaction.origin().name(),
                    transaction
            );
        }
    }

    @Override
    public Optional<Transaction> findByOriginName(String originName) {
        return Optional.ofNullable(transactionsByOrigin.get(originName));
    }
}
