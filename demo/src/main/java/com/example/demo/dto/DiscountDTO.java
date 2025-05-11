package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDTO {
    private Long id;
    private String productId;
    private String productName;
    private String brand;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Double percentageOfDiscount;
    private String storeName;
    private boolean isActive;
    private boolean isNew;
}