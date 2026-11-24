package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.Optional;

public record Transaction(
        int step,
        PaymentType type,
        BigDecimal amount,
        Customer origin,
        Customer destination,
        boolean isFraud,
        boolean isFlaggedFraud
) {

    public Transaction {
        type = Optional.ofNullable(type)
                .orElseThrow(() -> new IllegalArgumentException("type should not be null"));
        amount = Optional.ofNullable(amount)
                .orElseThrow(() -> new IllegalArgumentException("amount should not be null"));
        origin = Optional.ofNullable(origin)
                .orElseThrow(() -> new IllegalArgumentException("origin should not be null"));
        destination = Optional.ofNullable(destination)
                .orElseThrow(() -> new IllegalArgumentException("destination should not be null"));

        if (step < 1) {
            throw new IllegalArgumentException("step should be positive: " + step);
        }
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("amount should be positive: " + amount);
        }
    }
}
