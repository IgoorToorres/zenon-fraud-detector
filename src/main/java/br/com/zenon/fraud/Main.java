package br.com.zenon.fraud;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Customer origin = new Customer(
                "Igor",
                new BigDecimal("300.00"),
                new BigDecimal("250.00")
        );

        Customer destination = new Customer(
                "Julia",
                new BigDecimal("100.00"),
                new BigDecimal("150.00")
        );

        Transaction transaction = new Transaction(
                1,
                PaymentType.TRANSFER,
                new BigDecimal("50.00"),
                origin,
                destination,
                false,
                false
        );

        System.out.println(transaction);
    }
}
