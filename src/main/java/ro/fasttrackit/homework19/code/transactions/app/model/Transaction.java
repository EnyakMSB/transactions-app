package ro.fasttrackit.homework19.code.transactions.app.model;

public record Transaction(
        int id,
        String product,
        TransactionType type,
        double amount
) {
}


