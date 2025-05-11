package com.example.demo.controllers;

import com.example.demo.dto.CreateShoppingBasketRequest;
import com.example.demo.dto.ShoppingBasketDTO;
import com.example.demo.service.ShoppingBasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopping-baskets")
public class ShoppingBasketController {

    private final ShoppingBasketService shoppingBasketService;

    public ShoppingBasketController(ShoppingBasketService shoppingBasketService) {
        this.shoppingBasketService = shoppingBasketService;
    }

    @PostMapping
    public ShoppingBasketDTO createBasket(@RequestBody CreateShoppingBasketRequest request) {
        return shoppingBasketService.createBasket(request);
    }

    @GetMapping
    public List<ShoppingBasketDTO> getAllBaskets() {
        return shoppingBasketService.getAllBaskets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingBasketDTO> getBasket(@PathVariable Long id) {
        return shoppingBasketService.getBasket(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/optimize")
    public ResponseEntity<ShoppingBasketDTO> optimizeBasket(@PathVariable Long id) {
        try {
            ShoppingBasketDTO optimized = shoppingBasketService.optimizeBasket(id);
            return ResponseEntity.ok(optimized);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/products/{productId}")
    public ResponseEntity<ShoppingBasketDTO> addProductToBasket(
            @PathVariable Long id,
            @PathVariable String productId) {
        try {
            ShoppingBasketDTO updated = shoppingBasketService.addProductToBasket(id, productId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/products/{productId}")
    public ResponseEntity<ShoppingBasketDTO> removeProductFromBasket(
            @PathVariable Long id,
            @PathVariable String productId) {
        try {
            ShoppingBasketDTO updated = shoppingBasketService.removeProductFromBasket(id, productId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}