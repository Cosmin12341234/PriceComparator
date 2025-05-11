package com.example.demo.service;

import com.example.demo.dto.DiscountDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.mappers.DiscountMapper;
import com.example.demo.mappers.ProductMapper;
import com.example.demo.model.Discount;
import com.example.demo.model.Product;
import com.example.demo.model.Store;
import com.example.demo.repo.DiscountRepository;
import com.example.demo.repo.ProductRepository;
import com.example.demo.repo.StoreRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    //method to create a discount
    public List<DiscountDTO> getAllActiveDiscounts() {
        LocalDate today = LocalDate.now();
        List<Discount> discounts = discountRepository.findActiveDiscounts(today);
        return DiscountMapper.entityListToDto(discounts);
    }

    //method to get all new discounts (last 24 hours)
    public List<DiscountDTO> getNewDiscounts() {
        LocalDate today = LocalDate.now();
        List<Discount> discounts = discountRepository.findNewDiscounts(today);
        return DiscountMapper.entityListToDto(discounts);
    }

    public List<ProductDTO> getBestDiscounts(int limit) {
        LocalDate today = LocalDate.now();

        List<Discount> activeDiscounts = discountRepository.findActiveDiscounts(today);

        return activeDiscounts.stream()
                .sorted(Comparator.comparing(Discount::getPercentageOfDiscount).reversed())
                .limit(limit)
                .map(Discount::getProduct)
                .map(ProductMapper::entityToDto)
                .collect(Collectors.toList());
    }


}