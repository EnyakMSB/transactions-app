package ro.fasttrackit.homework19.code.transactions.app.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("ClassCanBeRecord")
@Service
public class TransactionReader {
    private final String fileLocation;

    public TransactionReader(@Value("${transactions.file}") String fileLocation) {
        this.fileLocation = fileLocation;

    }

    public List<Transaction> readTransactionsFromFile() throws IOException {
        return Files.lines(Path.of(fileLocation))
                .map(this::lineToTransaction)
                .collect(toList());
    }

    private Transaction lineToTransaction(String line) {
        String[] transactionsComponent = line.split("\\|");
        return new Transaction(Integer.parseInt(transactionsComponent[0]),
                transactionsComponent[1],
                TransactionType.valueOf(transactionsComponent[2]),
                Double.parseDouble(transactionsComponent[3]));
    }
}