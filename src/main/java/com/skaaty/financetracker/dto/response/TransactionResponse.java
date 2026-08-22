package com.skaaty.financetracker.dto.response;

import com.skaaty.financetracker.model.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private TransactionType type;

    private Long userId;
    private String categoryName;
}
