package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceAlertDTO {
    private Long id;
    private String productId;
    private String productName;
    private String storeName;
    private BigDecimal targetPrice;
    private BigDecimal currentPrice;
    private boolean triggered;
}