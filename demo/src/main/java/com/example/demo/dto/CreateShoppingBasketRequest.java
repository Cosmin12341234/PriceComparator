package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateShoppingBasketRequest {
    private String name;
    private List<String> productIds;
}