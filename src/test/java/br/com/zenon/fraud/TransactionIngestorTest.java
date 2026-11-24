package br.com.zenon.fraud;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionIngestorTest {

    private static final String HEADER =
            "step,type,amount,nameOrig,oldbalanceOrg,newbalanceOrig,"
                    + "nameDest,oldbalanceDest,newbalanceDest,isFraud,isFlaggedFraud";

    @TempDir
    Path tempDirectory;

    @Test
    void shouldLogInvalidLineAndContinueIngestion() throws Exception {
        String validBeforeError =
                "1,PAYMENT,10.00,C1,10.00,0.00,M1,0.00,0.00,0,0";
        String invalid =
                "0,TRANSFER,20.00,C2,20.00,0.00,C3,0.00,20.00,0,0";
        String validAfterError =
                "2,CASH_IN,30.00,C4,0.00,30.00,C5,0.00,0.00,1,0";
        Path csv = tempDirectory.resolve("transactions.csv");
        Files.writeString(
                csv,
                String.join(System.lineSeparator(),
                        HEADER, validBeforeError, invalid, validAfterError),
                UTF_8
        );

        ByteArrayOutputStream errorOutput = new ByteArrayOutputStream();
        PrintStream originalError = System.err;
        List<Transaction> transactions;

        try {
            System.setErr(new PrintStream(errorOutput, true, UTF_8));
            transactions = new TransactionIngestor().ingest(csv.toString());
        } finally {
            System.setErr(originalError);
        }

        assertEquals(2, transactions.size());
        assertEquals("C1", transactions.get(0).origin().name());
        assertEquals("C4", transactions.get(1).origin().name());
        assertTrue(errorOutput.toString(UTF_8).contains(
                "Erro: " + invalid
                        + " | java.lang.IllegalArgumentException: step should be positive: 0"
        ));
    }

    @Test
    void shouldReadAtMostOneThousandCsvLinesIncludingInvalidOnes() throws Exception {
        String invalid =
                "0,PAYMENT,1.00,C0,1.00,0.00,M0,0.00,0.00,0,0";
        StringBuilder csvContent = new StringBuilder(HEADER)
                .append(System.lineSeparator())
                .append(invalid);

        for (int index = 1; index <= 1_000; index++) {
            csvContent.append(System.lineSeparator())
                    .append("1,PAYMENT,1.00,C")
                    .append(index)
                    .append(",1.00,0.00,M")
                    .append(index)
                    .append(",0.00,0.00,0,0");
        }

        Path csv = tempDirectory.resolve("more-than-one-thousand.csv");
        Files.writeString(csv, csvContent, UTF_8);

        PrintStream originalError = System.err;
        List<Transaction> transactions;

        try {
            System.setErr(new PrintStream(new ByteArrayOutputStream(), true, UTF_8));
            transactions = new TransactionIngestor().ingest(csv.toString());
        } finally {
            System.setErr(originalError);
        }

        assertEquals(999, transactions.size());
        assertEquals(
                "C999",
                transactions.get(transactions.size() - 1).origin().name()
        );
    }

    @Test
    void shouldRespectRequestedLineLimit() throws Exception {
        Path csv = tempDirectory.resolve("custom-limit.csv");
        Files.writeString(
                csv,
                String.join(System.lineSeparator(),
                        HEADER,
                        "1,PAYMENT,10.00,C1,10.00,0.00,M1,0.00,0.00,0,0",
                        "1,TRANSFER,20.00,C2,20.00,0.00,C3,0.00,20.00,0,0",
                        "2,CASH_IN,30.00,C4,0.00,30.00,C5,0.00,0.00,0,0"),
                UTF_8
        );

        List<Transaction> transactions =
                new TransactionIngestor().ingest(csv.toString(), 2);

        assertEquals(2, transactions.size());
        assertEquals("C1", transactions.get(0).origin().name());
        assertEquals("C2", transactions.get(1).origin().name());
    }
}
