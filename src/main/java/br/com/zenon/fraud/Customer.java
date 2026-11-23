package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.Optional;

public record Customer(
        String name,
        BigDecimal oldBalance,
        BigDecimal newBalance
) {

    public Customer {
        name = Optional.ofNullable(name)
                .orElseThrow(() -> new IllegalArgumentException("name should not be null"));
        oldBalance = Optional.ofNullable(oldBalance)
                .orElseThrow(() -> new IllegalArgumentException("oldBalance should not be null"));
        newBalance = Optional.ofNullable(newBalance)
                .orElseThrow(() -> new IllegalArgumentException("newBalance should not be null"));

        if (name.isBlank()) {
            throw new IllegalArgumentException("name should not be empty");
        }
        if (oldBalance.signum() < 0) {
            throw new IllegalArgumentException(
                    "oldBalance should be positive: " + oldBalance
            );
        }
        if (newBalance.signum() < 0) {
            throw new IllegalArgumentException(
                    "newBalance should be positive: " + newBalance
            );
        }
    }
}
