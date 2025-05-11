package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingBasketDTO {
    private Long id;
    private String name;
    private List<ProductDTO> products;
    private BigDecimal totalCost;
    private Map<String, List<ProductDTO>> optimizedByStore;
    private BigDecimal optimizedTotalCost;
    private BigDecimal potentialSavings;
}