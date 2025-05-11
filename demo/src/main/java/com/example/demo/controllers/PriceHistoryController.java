package com.example.demo.controllers;

import com.example.demo.dto.PriceHistoryDTO;
import com.example.demo.service.PriceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/price-history")
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    public PriceHistoryController(PriceHistoryService priceHistoryService) {
        this.priceHistoryService = priceHistoryService;
    }

    @GetMapping("/product/{productId}")
    public List<PriceHistoryDTO> getProductPriceHistory(@PathVariable String productId) {
        return priceHistoryService.getProductPriceHistory(productId);
    }

    @GetMapping("/product/{productId}/range")
    public List<PriceHistoryDTO> getProductPriceHistoryInDateRange(
            @PathVariable String productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return priceHistoryService.getProductPriceHistoryInDateRange(productId, startDate, endDate);
    }
}