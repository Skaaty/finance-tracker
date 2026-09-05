package com.skaaty.financetracker.service;

import com.skaaty.financetracker.model.Portfolio;
import com.skaaty.financetracker.model.StockPosition;
import com.skaaty.financetracker.repository.PortfolioRepository;
import com.skaaty.financetracker.repository.StockPositionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.Port;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class XtbParserService {

    private final PortfolioRepository portfolioRepository;
    private final StockPositionRepository stockPositionRepository;

    @Transactional
    public void parseAndImportXtbStatement(MultipartFile file, Long userId) throws Exception {
        Portfolio portfolio = portfolioRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found for user id: " + userId));

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
        }
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

            if (isDataRow && firstCell != null && !isCellEmpty(firstCell))
        }
    }

    private boolean isCellEmpty(Cell cell) {
        return cell.getCellType() == CellType.BLANK ||
                (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty());
    }
}
