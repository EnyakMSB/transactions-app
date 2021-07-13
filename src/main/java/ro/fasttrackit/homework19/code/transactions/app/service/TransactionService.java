package ro.fasttrackit.homework19.code.transactions.app.service;

import org.springframework.stereotype.Service;
import ro.fasttrackit.homework19.code.transactions.app.model.Transaction;
import ro.fasttrackit.homework19.code.transactions.app.model.TransactionReader;
import ro.fasttrackit.homework19.code.transactions.app.model.TransactionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
public class TransactionService {
    private final List<Transaction> transactions = new ArrayList<>();

    public TransactionService(TransactionReader transactionReader) throws Exception {
        this.transactions.addAll(transactionReader.readTransactionsFromFile());
    }

    public List<Transaction> getAllTransactionsFilterable(String productName, TransactionType transactionType,
                                                          Double maxAmount, Double minAmount) {
        return transactions.stream()
                .filter(transaction -> productName == null || transaction.product().equalsIgnoreCase(productName))
                .filter(transaction -> transactionType == null || transaction.type() == transactionType)
                .filter(transaction -> maxAmount == null || transaction.amount() <= maxAmount)
                .filter(transaction -> minAmount == null || transaction.amount() >= minAmount)
                .collect(toList());
    }

    public Optional<Transaction> getById(int productId) {
        return transactions.stream()
                .filter(transaction -> transaction.id() == productId)
                .findFirst();
    }

    public Transaction addTransaction(Transaction transaction) {
        return addTransaction(transaction, maxId() + 1);
    }

    private int maxId() {
        return this.transactions.stream()
                .mapToInt(Transaction::id)
                .max()
                .orElse(1);
    }

    public Transaction addTransaction(Transaction transaction, int transactionId) {
        Transaction newTransaction = new Transaction(
                transactionId,
                transaction.product(),
                transaction.type(),
                transaction.amount()
        );
        this.transactions.add(transactionId - 1, newTransaction);
        return newTransaction;
    }


    public Optional<Transaction> replaceTransaction(int transactionId, Transaction newTransaction) {
        Optional<Transaction> replacedTransaction = deleteTransaction(transactionId);
        replacedTransaction
                .ifPresent(deletedTransaction -> addTransaction(newTransaction, transactionId));
        return replacedTransaction;
    }

    public Optional<Transaction> deleteTransaction(int transactionId) {
        Optional<Transaction> transactionOptional = getById(transactionId);
        transactionOptional
                .ifPresent(transactions::remove);
        return transactionOptional;
    }

    public Optional<Transaction> patchTransaction(int transactionId, Transaction transaction) {
        Optional<Transaction> transactionById = getById(transactionId);
        Optional<Transaction> patchedTransaction = transactionById.
                map(oldTransaction -> new Transaction(
                        oldTransaction.id(),
                        transaction.product() != null ? transaction.product() : oldTransaction.product(),
                        oldTransaction.type(),
                        transaction.amount() != 0 ? transaction.amount() : oldTransaction.amount()
                ));
        patchedTransaction.ifPresent(newTransaction -> replaceTransaction(transactionId, newTransaction));
        return patchedTransaction;
    }


    public Map<TransactionType, List<Double>> typeReport() {
        return transactions.stream()
                .collect(groupingBy(Transaction::type, mapping(Transaction::amount, toList())
                ));
    }

    public Map<TransactionType, Double> sumTypeAmount() {
        return transactions.stream()
                .collect(groupingBy(Transaction::type, summingDouble(Transaction::amount)
                ));
    }

    public Map<String, List<Double>> productReport() {
        return transactions.stream()
                .collect(groupingBy(Transaction::product, mapping(Transaction::amount, toList())
                ));
    }

    public Map<String, Double> sumProductAmount() {
        return transactions.stream()
                .collect(groupingBy(Transaction::product, summingDouble(Transaction::amount)
                ));
    }
}
