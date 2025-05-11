package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceHistoryDTO {
    private String productId;
    private String productName;
    private String storeName;
    private LocalDate date;
    private BigDecimal price;
}