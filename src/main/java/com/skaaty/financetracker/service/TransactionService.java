package com.skaaty.financetracker.service;

import com.skaaty.financetracker.model.User;
import com.skaaty.financetracker.model.Category;
import com.skaaty.financetracker.model.Transaction;
import com.skaaty.financetracker.repository.TransactionRepository;
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

    public Transaction addTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getUserTransactions(Long userId) {
        return transactionRepository.findByUserId(userId);
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
