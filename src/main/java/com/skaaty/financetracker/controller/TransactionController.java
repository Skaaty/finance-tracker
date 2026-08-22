package com.skaaty.financetracker.controller;

import com.skaaty.financetracker.dto.request.TransactionRequest;
import com.skaaty.financetracker.dto.response.TransactionResponse;
import com.skaaty.financetracker.model.Category;
import com.skaaty.financetracker.model.User;
import com.skaaty.financetracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> addTransaction(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.addTransaction(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionResponse>> getUserTransactions(@PathVariable Long userId) {
        List<TransactionResponse> transactions = transactionService.getUserTransactions(userId);
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
