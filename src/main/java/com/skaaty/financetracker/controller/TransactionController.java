package com.skaaty.financetracker.controller;

import com.skaaty.financetracker.model.Category;
import com.skaaty.financetracker.model.User;
import com.skaaty.financetracker.model.Transaction;
import com.skaaty.financetracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionService.addTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getUserTransactions(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.getUserTransactions(userId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadCsv(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam("userId") Long userId,
            @RequestParam("categoryId") Long categoryId) {

        try {
            User user = new User();
            user.setId(userId);

            Category category = new Category();
            category.setId(categoryId);

            transactionService.importCsv(file, user, category);

            return ResponseEntity.ok("File uploaded and processed successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Could not process the file: " + e.getMessage());
        }
    }
}
