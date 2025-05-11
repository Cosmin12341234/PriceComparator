package com.example.demo.service;

import com.example.demo.dto.CreateShoppingBasketRequest;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ShoppingBasketDTO;
import com.example.demo.mappers.ShoppingBasketMapper;
import com.example.demo.model.Product;
import com.example.demo.model.ShoppingBasket;
import com.example.demo.repo.ProductRepository;
import com.example.demo.repo.ShoppingBasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShoppingBasketService {

    private final ShoppingBasketRepository shoppingBasketRepository;

    private final ProductRepository productRepository;

    private final ProductService productService;

    public ShoppingBasketService(ShoppingBasketRepository shoppingBasketRepository, ProductRepository productRepository, ProductService productService) {
        this.shoppingBasketRepository = shoppingBasketRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    public ShoppingBasketDTO createBasket(CreateShoppingBasketRequest request) {
        ShoppingBasket basket = new ShoppingBasket();
        basket.setName(request.getName());

        for (String id : request.getProductIds()) {
            ShoppingBasket finalBasket = basket;
            productRepository.findById(id).ifPresent(p -> finalBasket.getProducts().add(p));
        }

        basket = shoppingBasketRepository.save(basket);
        return ShoppingBasketMapper.entityToDto(basket);
    }

    public List<ShoppingBasketDTO> getAllBaskets() {
        List<ShoppingBasket> baskets = shoppingBasketRepository.findAll();
        return ShoppingBasketMapper.entityListToDto(baskets);
    }

    public Optional<ShoppingBasketDTO> getBasket(Long id) {
        return shoppingBasketRepository.findById(id)
                .map(ShoppingBasketMapper::entityToDto);
    }

    public void deleteBasket(Long id) {
        shoppingBasketRepository.deleteById(id);
    }

    public ShoppingBasketDTO optimizeBasket(Long basketId) {
        Optional<ShoppingBasket> basketOpt = shoppingBasketRepository.findById(basketId);
        if (basketOpt.isEmpty()) {
            throw new IllegalArgumentException("Basket not found with ID: " + basketId);
        }

        ShoppingBasket basket = basketOpt.get();
        ShoppingBasketDTO dto = ShoppingBasketMapper.entityToDto(basket);

        Map<String, List<ProductDTO>> optimizedByStore = new HashMap<>();
        BigDecimal optimizedTotalCost = BigDecimal.ZERO;

        for (Product originalProduct : basket.getProducts()) {
            List<ProductDTO> allVersions = productService.findSubstitutes(originalProduct.getProductId());
            if (allVersions.isEmpty()) {
                ProductDTO productDTO = productService.getProductById(originalProduct.getProductId()).orElse(null);
                if (productDTO != null) {
                    String storeName = productDTO.getStoreName();
                    optimizedByStore.computeIfAbsent(storeName, k -> new java.util.ArrayList<>()).add(productDTO);
                    optimizedTotalCost = optimizedTotalCost.add(productDTO.getPrice());
                }
            } else {
                ProductDTO cheapest = allVersions.stream()
                        .min(Comparator.comparing(ProductDTO::getPrice))
                        .orElse(null);

                if (cheapest != null) {
                    String storeName = cheapest.getStoreName();
                    optimizedByStore.computeIfAbsent(storeName, k -> new java.util.ArrayList<>()).add(cheapest);
                    optimizedTotalCost = optimizedTotalCost.add(cheapest.getPrice());
                }
            }
        }

        dto.setOptimizedByStore(optimizedByStore);
        dto.setOptimizedTotalCost(optimizedTotalCost);
        dto.setPotentialSavings(dto.getTotalCost().subtract(optimizedTotalCost));

        return dto;
    }

    public ShoppingBasketDTO addProductToBasket(Long basketId, String productId) {
        ShoppingBasket basket = shoppingBasketRepository.findById(basketId)
                .orElseThrow(() -> new IllegalArgumentException("Basket not found with ID: " + basketId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        basket.getProducts().add(product);
        basket = shoppingBasketRepository.save(basket);

        return ShoppingBasketMapper.entityToDto(basket);
    }

    public ShoppingBasketDTO removeProductFromBasket(Long basketId, String productId) {
        ShoppingBasket basket = shoppingBasketRepository.findById(basketId)
                .orElseThrow(() -> new IllegalArgumentException("Basket not found with ID: " + basketId));

        basket.getProducts().removeIf(p -> p.getProductId().equals(productId));
        basket = shoppingBasketRepository.save(basket);

        return ShoppingBasketMapper.entityToDto(basket);
    }
}