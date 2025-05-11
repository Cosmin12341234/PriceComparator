package com.example.demo.mappers;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ShoppingBasketDTO;
import com.example.demo.model.ShoppingBasket;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public final class ShoppingBasketMapper {

    public static ShoppingBasketDTO entityToDto(ShoppingBasket basket) {
        if (basket == null) return null;

        ShoppingBasketDTO dto = new ShoppingBasketDTO();
        dto.setId(basket.getId());
        dto.setName(basket.getName());

        List<ProductDTO> productDTOs = basket.getProducts().stream()
                .map(ProductMapper::entityToDto)
                .collect(Collectors.toList());

        dto.setProducts(productDTOs);

        BigDecimal totalCost = productDTOs.stream()
                .map(ProductDTO::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        dto.setTotalCost(totalCost);

        return dto;
    }

    public static List<ShoppingBasketDTO> entityListToDto(List<ShoppingBasket> baskets) {
        if (baskets == null) return List.of();

        return baskets.stream()
                .map(ShoppingBasketMapper::entityToDto)
                .collect(Collectors.toList());
    }
}