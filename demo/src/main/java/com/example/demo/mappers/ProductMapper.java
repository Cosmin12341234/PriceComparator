package com.example.demo.mappers;

import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Component
public final class ProductMapper {

    public static ProductDTO entityToDto(Product product) {
        if (product == null) return null;

        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductCategory(product.getProductCategory());
        dto.setBrand(product.getBrand());
        dto.setPackageQuantity(product.getPackageQuantity());
        dto.setPackageUnit(product.getPackageUnit());
        dto.setPrice(product.getPrice());
        dto.setCurrency(product.getCurrency());
        dto.setStoreName(product.getStore().getName());

        if (product.getPackageQuantity() != null && product.getPackageQuantity() > 0) {
            dto.setUnitPrice(product.getPrice().divide(
                    BigDecimal.valueOf(product.getPackageQuantity()),
                    2, RoundingMode.HALF_UP));
        } else {
            dto.setUnitPrice(product.getPrice());
        }

        return dto;
    }

    public static List<ProductDTO> entityListToDto(List<Product> products) {
        if (products == null) return List.of();

        return products.stream()
                .map(ProductMapper::entityToDto)
                .collect(Collectors.toList());
    }
}