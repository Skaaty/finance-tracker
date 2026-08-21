package com.skaaty.financetracker.service;

import com.skaaty.financetracker.model.Transaction;
import com.skaaty.financetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
