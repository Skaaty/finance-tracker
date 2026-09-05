package com.skaaty.financetracker.repository;

import com.skaaty.financetracker.model.StockPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockPositionRepository extends JpaRepository<StockPosition, Long> {
    List<StockPosition> findByPortfolioId(Long portfolioId);

    Optional<StockPosition> findByPortfolioIdAndTickerSymbol(Long portfolioId, String tickerSymbol);
}
