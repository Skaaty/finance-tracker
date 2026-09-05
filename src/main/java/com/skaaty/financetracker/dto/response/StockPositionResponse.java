package com.skaaty.financetracker.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class StockPositionResponse {
    private String tickerSymbol;
    private BigDecimal quantity;
    private BigDecimal averageBuyPrice;
}
