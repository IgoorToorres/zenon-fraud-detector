package br.com.zenon.fraud;

import br.com.zenon.fraud.PaymentType;

import java.math.BigDecimal;

public record Transaction(
        int step,
        PaymentType type,
        BigDecimal amount,
        Customer origin,
        Customer destination,
        boolean isFraud,
        boolean isFlaggedFraud
) {
}
