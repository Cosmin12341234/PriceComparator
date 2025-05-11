package com.example.demo.controllers;

import com.example.demo.dto.DiscountDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping("/active")
    public List<DiscountDTO> getActiveDiscounts() {
        return discountService.getAllActiveDiscounts();
    }

    @GetMapping("/new")
    public List<DiscountDTO> getNewDiscounts() {
        return discountService.getNewDiscounts();
    }

    @GetMapping("/best")
    public List<ProductDTO> getBestDiscounts(@RequestParam(defaultValue = "10") int limit) {
        return discountService.getBestDiscounts(limit);
    }
}