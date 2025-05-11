package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.mappers.ProductMapper;
import com.example.demo.model.Product;
import com.example.demo.model.Store;
import com.example.demo.repo.ProductRepository;
import com.example.demo.repo.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ProductMapper.entityListToDto(products);
    }

    public Optional<ProductDTO> getProductById(String id) {
        return productRepository.findById(id)
                .map(ProductMapper::entityToDto);
    }

    public List<ProductDTO> findSubstitutes(String productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            return List.of();
        }

        Product product = productOpt.get();

        List<Product> sameProductDifferentStores = productRepository.findSameProductInOtherStores(
                product.getProductName(), product.getStore().getName());


        List<ProductDTO> substitutes = ProductMapper.entityListToDto(sameProductDifferentStores);
        substitutes.sort(Comparator.comparing(ProductDTO::getUnitPrice));

        return substitutes;
    }
}