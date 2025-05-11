package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePriceAlertRequest {
    private String productId;
    private BigDecimal targetPrice;
}