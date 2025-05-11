package com.example.demo.mappers;

import com.example.demo.dto.PriceHistoryDTO;
import com.example.demo.model.PriceHistory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public final class PriceHistoryMapper {

    public static PriceHistoryDTO entityToDto(PriceHistory priceHistory) {
        if (priceHistory == null) return null;

        PriceHistoryDTO dto = new PriceHistoryDTO();
        dto.setProductId(priceHistory.getProduct().getProductId());
        dto.setProductName(priceHistory.getProduct().getProductName());
        dto.setStoreName(priceHistory.getProduct().getStore().getName());
        dto.setDate(priceHistory.getDate());
        dto.setPrice(priceHistory.getPrice());

        return dto;
    }

    public static List<PriceHistoryDTO> entityListToDto(List<PriceHistory> priceHistories) {
        if (priceHistories == null) return List.of();

        return priceHistories.stream()
                .map(PriceHistoryMapper::entityToDto)
                .collect(Collectors.toList());
    }
}