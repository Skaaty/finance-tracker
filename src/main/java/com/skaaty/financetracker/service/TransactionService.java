package com.skaaty.financetracker.service;

import com.skaaty.financetracker.dto.request.TransactionRequest;
import com.skaaty.financetracker.dto.response.TransactionResponse;
import com.skaaty.financetracker.model.User;
import com.skaaty.financetracker.model.Category;
import com.skaaty.financetracker.model.Transaction;
import com.skaaty.financetracker.repository.CategoryRepository;
import com.skaaty.financetracker.repository.TransactionRepository;
import com.skaaty.financetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public TransactionResponse addTransaction(TransactionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getUserId()));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with the id: " + request.getCategoryId()));

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setDate(request.getDate());
        transaction.setDescription(request.getDescription());
        transaction.setType(request.getType());
        transaction.setUser(user);
        transaction.setCategory(category);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return mapToResponse(savedTransaction);
    }

    public List<TransactionResponse> getUserTransactions(Long userId) {
        return transactionRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .date(transaction.getDate())
                .description(transaction.getDescription())
                .type(transaction.getType())
                .userId(transaction.getUser().getId())
                .categoryName(transaction.getCategory().getName())
                .build();
    }

    public void importCsv(MultipartFile file, User user, Category defaultCategory) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                Transaction transaction = new Transaction();
                transaction.setDate(java.time.LocalDate.parse(data[0]));
                transaction.setAmount(new java.math.BigDecimal(data[1]));
                transaction.setDescription(data[2]);
                transaction.setType(com.skaaty.financetracker.model.TransactionType.valueOf(data[3].toUpperCase()));

                transaction.setUser(user);
                transaction.setCategory(defaultCategory);

                transactionRepository.save(transaction);
            }
        }
    }
}
