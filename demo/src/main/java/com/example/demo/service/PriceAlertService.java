package com.example.demo.service;

import com.example.demo.dto.CreatePriceAlertRequest;
import com.example.demo.dto.PriceAlertDTO;
import com.example.demo.mappers.PriceAlertMapper;
import com.example.demo.model.PriceAlert;
import com.example.demo.model.Product;
import com.example.demo.repo.PriceAlertRepository;
import com.example.demo.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PriceAlertService {

    private final PriceAlertRepository priceAlertRepository;

    private final ProductRepository productRepository;

    public PriceAlertService(PriceAlertRepository priceAlertRepository, ProductRepository productRepository) {
        this.priceAlertRepository = priceAlertRepository;
        this.productRepository = productRepository;
    }

    public PriceAlertDTO createAlert(CreatePriceAlertRequest request) {
        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found with ID: " + request.getProductId());
        }

        Product product = productOpt.get();
        PriceAlert alert = new PriceAlert();
        alert.setProduct(product);
        alert.setTargetPrice(request.getTargetPrice());
        alert.setTriggered(false);

        if (product.getPrice().compareTo(request.getTargetPrice()) <= 0) {
            alert.setTriggered(true);
        }

        alert = priceAlertRepository.save(alert);
        return PriceAlertMapper.entityToDto(alert);
    }

    public List<PriceAlertDTO> getAllAlerts() {
        List<PriceAlert> alerts = priceAlertRepository.findAll();
        return PriceAlertMapper.entityListToDto(alerts);
    }

    public List<PriceAlertDTO> getTriggeredAlerts() {
        List<PriceAlert> alerts = priceAlertRepository.findAll().stream()
                .filter(PriceAlert::isTriggered)
                .toList();
        return PriceAlertMapper.entityListToDto(alerts);
    }

    public Optional<PriceAlertDTO> getAlertById(Long id) {
        return priceAlertRepository.findById(id)
                .map(PriceAlertMapper::entityToDto);
    }

    public void deleteAlert(Long id) {
        priceAlertRepository.deleteById(id);
    }
}