package com.skaaty.financetracker.scheduler;

import com.skaaty.financetracker.model.MonthlySummary;
import com.skaaty.financetracker.model.Transaction;
import com.skaaty.financetracker.model.TransactionType;
import com.skaaty.financetracker.model.User;
import com.skaaty.financetracker.repository.MonthlySummaryRepository;
import com.skaaty.financetracker.repository.TransactionRepository;
import com.skaaty.financetracker.repository.UserRepository;
import com.skaaty.financetracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnalyticsScheduler {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final MonthlySummaryRepository monthlySummaryRepository;

    @Scheduled(fixedRate = 30000)
    public void generateMonthlySummaries() {
        log.info("Starting automated summary generation...");

        List<User> users = userRepository.findAll();
        for (User user : users) {
            List<Transaction> transactions = transactionRepository.findByUserId(user.getId());

            if (transactions.isEmpty()) continue;

            BigDecimal income = BigDecimal.ZERO;
            BigDecimal expense = BigDecimal.ZERO;

            for (Transaction t : transactions) {
                if (t.getType() == TransactionType.INCOME) {
                    income = income.add(t.getAmount());
                } else {
                    expense = expense.add(t.getAmount());
                }
            }

            MonthlySummary summary = new MonthlySummary();
            summary.setUser(user);
            summary.setTotalIncome(income);
            summary.setTotalExpense(expense);
            summary.setGeneratedAt(LocalDateTime.now());

            monthlySummaryRepository.save(summary);
            log.info("Generated summary for user {}: Income={}, Expense={}", user.getUsername(), income, expense);
        }
    }
}
