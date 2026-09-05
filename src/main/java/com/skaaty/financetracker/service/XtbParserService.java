package com.skaaty.financetracker.service;

import com.skaaty.financetracker.dto.response.PortfolioResponse;
import com.skaaty.financetracker.dto.response.StockPositionResponse;
import com.skaaty.financetracker.model.Portfolio;
import com.skaaty.financetracker.model.StockPosition;
import com.skaaty.financetracker.repository.PortfolioRepository;
import com.skaaty.financetracker.repository.StockPositionRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class XtbParserService {

    private final PortfolioRepository portfolioRepository;
    private final StockPositionRepository stockPositionRepository;

    @Transactional
    public void parseAndImportXtbStatement(MultipartFile file, Long userId) throws Exception {
        Portfolio portfolio = portfolioRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found for user id: " + userId));

        portfolio.setCashBalance(BigDecimal.ZERO);

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet cashSheet = getSheetByNameContains(workbook, "Cash", "Gotówk");
            if (cashSheet != null) {
                processCashOperations(cashSheet, portfolio);
            }

            Sheet openPositionsSheet = getSheetByNameContains(workbook, "Open", "Otwarte");
            if (openPositionsSheet != null) {
                processOpenPositions(openPositionsSheet, portfolio);
            }

            portfolioRepository.save(portfolio);
        }
    }

    @Transactional(readOnly = true)
    public PortfolioResponse getPortfolioByUserId(Long userId) {
        Portfolio portfolio = portfolioRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found for user id: " + userId));

        List<StockPositionResponse> positionResponses = stockPositionRepository.findByPortfolioId(portfolio.getId())
                .stream()
                .map(pos -> StockPositionResponse.builder()
                        .tickerSymbol(pos.getTickerSymbol())
                        .quantity(pos.getQuantity())
                        .averageBuyPrice(pos.getAverageBuyPrice())
                        .build())
                .toList();

        return PortfolioResponse.builder()
                .id(portfolio.getId())
                .cashBalance(portfolio.getCashBalance())
                .positions(positionResponses)
                .build();
    }

    private void processCashOperations(Sheet sheet, Portfolio portfolio) {
        boolean isDataRow = false;

        for (Row row : sheet) {
            Cell firstCell = row.getCell(0);

            if (firstCell != null && firstCell.getCellType() == CellType.STRING &&
                firstCell.getStringCellValue().trim().equalsIgnoreCase("Type")) {
                isDataRow = true;
                continue;
            }

            if (isDataRow && firstCell != null && isCellEmpty(firstCell)) {

                if (firstCell.getCellType() == CellType.STRING) {
                    String rowType = firstCell.getStringCellValue().trim().toLowerCase();
                    if (rowType.contains("total")) break;
                }

                Cell amountCell = row.getCell(5);
                BigDecimal amount = getBigDecimalFromCell(amountCell);

                if (amount != null) {
                    portfolio.setCashBalance(portfolio.getCashBalance().add(amount));
                }
            }
        }
    }

    private void processOpenPositions(Sheet sheet, Portfolio portfolio) {
        boolean isDataRow = false;

        for (Row row : sheet) {
            Cell firstCell = row.getCell(0);
            Cell tickerHeaderCell = row.getCell(2);

            if (firstCell != null && firstCell.getCellType() == CellType.STRING &&
                    firstCell.getStringCellValue().trim().equalsIgnoreCase("Product") &&
                    tickerHeaderCell != null && tickerHeaderCell.getStringCellValue().trim().equalsIgnoreCase("Ticker")) {
                isDataRow = true;
                continue;
            }

            if (isDataRow) {
                Cell tickerCell = row.getCell(2); // ticker

                if (tickerCell != null && isCellEmpty(tickerCell)) {
                    String ticker = tickerCell.getStringCellValue().trim();

                    Cell volumeCell = row.getCell(5);
                    Cell priceCell = row.getCell(8);

                    BigDecimal volume = getBigDecimalFromCell(volumeCell);
                    BigDecimal openPrice = getBigDecimalFromCell(priceCell);

                    if (volume != null && openPrice != null) {
                        StockPosition position = stockPositionRepository
                                .findByPortfolioIdAndTickerSymbol(portfolio.getId(), ticker)
                                .orElse(StockPosition.builder()
                                        .portfolio(portfolio)
                                        .tickerSymbol(ticker)
                                        .quantity(BigDecimal.ZERO)
                                        .averageBuyPrice(BigDecimal.ZERO)
                                        .build());

                        position.setQuantity(volume);
                        position.setAverageBuyPrice(openPrice);

                        stockPositionRepository.save(position);
                    }
                }
            }
        }
    }
    private BigDecimal getBigDecimalFromCell(Cell cell) {
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.STRING) {
            String val = cell.getStringCellValue().trim().replace(" ", "").replace(",", ".");
            if (val.isEmpty()) return null;
            try {
                return new BigDecimal(val);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private boolean isCellEmpty(Cell cell) {
        return cell.getCellType() != CellType.BLANK &&
                (cell.getCellType() != CellType.STRING || !cell.getStringCellValue().trim().isEmpty());
    }

    private Sheet getSheetByNameContains(Workbook workbook, String... keywords) {
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            String sheetName = workbook.getSheetName(i).toLowerCase();
            for (String keyword : keywords) {
                if (sheetName.contains(keyword.toLowerCase())) {
                    return workbook.getSheetAt(i);
                }
            }
        }
        return null;
    }
}
