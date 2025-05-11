package com.example.demo.service;

import com.example.demo.dto.PriceHistoryDTO;
import com.example.demo.mappers.PriceHistoryMapper;
import com.example.demo.model.PriceAlert;
import com.example.demo.model.PriceHistory;
import com.example.demo.model.Product;
import com.example.demo.repo.PriceAlertRepository;
import com.example.demo.repo.PriceHistoryRepository;
import com.example.demo.repo.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;

    private final ProductRepository productRepository;

    private final PriceAlertRepository priceAlertRepository;

    public PriceHistoryService(PriceHistoryRepository priceHistoryRepository, ProductRepository productRepository, PriceAlertRepository priceAlertRepository) {
        this.priceHistoryRepository = priceHistoryRepository;
        this.productRepository = productRepository;
        this.priceAlertRepository = priceAlertRepository;
    }

    public void recordPrice(Product product, LocalDate date) {
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setProduct(product);
        priceHistory.setDate(date);
        priceHistory.setPrice(product.getPrice());

        priceHistoryRepository.save(priceHistory);

        checkPriceAlerts(product);
    }

    public List<PriceHistoryDTO> getProductPriceHistory(String productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            return List.of();
        }

        Product product = productOpt.get();
        List<PriceHistory> history = priceHistoryRepository.findByProductOrderByDateAsc(product);
        return PriceHistoryMapper.entityListToDto(history);
    }

    public List<PriceHistoryDTO> getProductPriceHistoryInDateRange(String productId, LocalDate start, LocalDate end) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            return List.of();
        }

        Product product = productOpt.get();
        List<PriceHistory> history = priceHistoryRepository.findByProductAndDateBetweenOrderByDateAsc(product, start, end);
        return PriceHistoryMapper.entityListToDto(history);
    }

    private void checkPriceAlerts(Product product) {
        List<PriceAlert> alerts = priceAlertRepository.findByProductAndTriggeredFalse(product);

        for (PriceAlert alert : alerts) {
            if (product.getPrice().compareTo(alert.getTargetPrice()) <= 0) {
                alert.setTriggered(true);
                priceAlertRepository.save(alert);
            }
        }
    }
}