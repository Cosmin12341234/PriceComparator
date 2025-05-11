package com.example.demo.mappers;

import com.example.demo.dto.PriceAlertDTO;
import com.example.demo.model.PriceAlert;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public final class PriceAlertMapper {

    public static PriceAlertDTO entityToDto(PriceAlert alert) {
        if (alert == null) return null;

        PriceAlertDTO dto = new PriceAlertDTO();
        dto.setId(alert.getId());
        dto.setProductId(alert.getProduct().getProductId());
        dto.setProductName(alert.getProduct().getProductName());
        dto.setStoreName(alert.getProduct().getStore().getName());
        dto.setTargetPrice(alert.getTargetPrice());
        dto.setCurrentPrice(alert.getProduct().getPrice());
        dto.setTriggered(alert.isTriggered());

        return dto;
    }

    public static List<PriceAlertDTO> entityListToDto(List<PriceAlert> alerts) {
        if (alerts == null) return List.of();

        return alerts.stream()
                .map(PriceAlertMapper::entityToDto)
                .collect(Collectors.toList());
    }
}