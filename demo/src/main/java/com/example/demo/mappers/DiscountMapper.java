package com.example.demo.mappers;

import com.example.demo.dto.DiscountDTO;
import com.example.demo.model.Discount;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public final class DiscountMapper {

    public static DiscountDTO entityToDto(Discount discount) {
        if (discount == null) return null;

        LocalDate today = LocalDate.now();

        DiscountDTO dto = new DiscountDTO();
        dto.setId(discount.getId());
        dto.setProductId(discount.getProduct().getProductId());
        dto.setProductName(discount.getProduct().getProductName());
        dto.setBrand(discount.getProduct().getBrand());
        dto.setFromDate(discount.getFromDate());
        dto.setToDate(discount.getToDate());
        dto.setPercentageOfDiscount(discount.getPercentageOfDiscount());
        dto.setStoreName(discount.getProduct().getStore().getName());

        boolean isActive = (today.isEqual(discount.getFromDate()) ||
                today.isEqual(discount.getToDate()) ||
                (today.isAfter(discount.getFromDate()) &&
                        today.isBefore(discount.getToDate())));
        dto.setActive(isActive);

        dto.setNew(discount.getCreatedDate().isEqual(LocalDate.now()));

        return dto;
    }

    public static List<DiscountDTO> entityListToDto(List<Discount> discounts) {
        if (discounts == null) return List.of();

        return discounts.stream()
                .map(DiscountMapper::entityToDto)
                .collect(Collectors.toList());
    }
}