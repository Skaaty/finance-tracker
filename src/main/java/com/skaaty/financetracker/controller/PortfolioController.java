package com.skaaty.financetracker.controller;

import com.skaaty.financetracker.dto.response.PortfolioResponse;
import com.skaaty.financetracker.service.XtbParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final XtbParserService xtbParserService;

    @GetMapping("/{userId}")
    public ResponseEntity<PortfolioResponse> getPortfolio(@PathVariable Long userId) {
        PortfolioResponse response = xtbParserService.getPortfolioByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/{userId}/upload-xtb", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadXtbStatement(
            @PathVariable Long userId,
            @RequestParam("file")MultipartFile file) {
        try {
            xtbParserService.parseAndImportXtbStatement(file, userId);
            return ResponseEntity.ok("XTB portfolio statement successfully parsed and balances updated.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error parsing XTB statement: " + e.getMessage());
        }
    }
}
