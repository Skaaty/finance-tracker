package com.skaaty.financetracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "stock_positions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tickerSymbol;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal quantity; //Number of shaers (can be fractional)

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal averageBuyPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;
}
