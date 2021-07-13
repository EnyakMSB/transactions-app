package ro.fasttrackit.homework19.code.transactions.app.controller;

import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.homework19.code.transactions.app.model.Transaction;
import ro.fasttrackit.homework19.code.transactions.app.model.TransactionType;
import ro.fasttrackit.homework19.code.transactions.app.service.TransactionService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    //localhost:8080/transactions?productName=monitor&transactionTypee=SELL&minAmount=0.01&maxAmount=800
    public List<Transaction> multiFilters(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) Double minAmount) {
        return transactionService.getAllTransactionsFilterable(productName, transactionType, maxAmount, minAmount);
    }

    //localhost:8080/transactions/4

    @GetMapping("/{productId}")
    public Optional<Transaction> getById(@PathVariable int productId) {
        return transactionService.getById(productId);
    }

    @PostMapping
    Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.addTransaction(transaction);
    }

    @PutMapping("{transactionId}")
    Transaction replaceTransaction(@PathVariable int transactionId, @RequestBody Transaction newTransaction) {
        return transactionService.replaceTransaction(transactionId, newTransaction)
                .orElse(null);
    }

    @PatchMapping("{transactionId}")
    Transaction patchTransaction(@PathVariable int transactionId, @RequestBody Transaction transaction) {
        return transactionService.patchTransaction(transactionId, transaction)
                .orElse(null);
    }

    @DeleteMapping("{transactionId}")
    Transaction deleteTransaction(@PathVariable int transactionId) {
        return transactionService.deleteTransaction(transactionId)
                .orElse(null);
    }

    //localhost:8080/transactions/reports/type
    @GetMapping("/reports/type")
    Map<TransactionType, List<Double>> typeReport() {
        return transactionService.typeReport();
    }

    //localhost:8080/transactions/reports/type/sum
    @GetMapping("/reports/type/sum")
    Map<TransactionType, Double> sumTypeAmount() {
        return transactionService.sumTypeAmount();
    }

    //localhost:8080/transactions/reports/product
    @GetMapping("/reports/product")
    Map<String, List<Double>> productReport() {
        return transactionService.productReport();
    }

    //localhost:8080/transactions/reports/product/sum
    @GetMapping("/reports/product/sum")
    Map<String, Double> sumProductAmount() {
        return transactionService.sumProductAmount();
    }
}

