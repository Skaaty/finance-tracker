package com.skaaty.financetracker.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class PortfolioResponse {
    private Long id;
    private BigDecimal cashBalance;
    private List<StockPositionResponse> positions;
}
