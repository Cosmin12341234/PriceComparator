package com.example.demo.controllers;

import com.example.demo.dto.CreatePriceAlertRequest;
import com.example.demo.dto.PriceAlertDTO;
import com.example.demo.service.PriceAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/price-alerts")
public class PriceAlertController {

    private final PriceAlertService priceAlertService;

    public PriceAlertController(PriceAlertService priceAlertService) {
        this.priceAlertService = priceAlertService;
    }

    @PostMapping
    public PriceAlertDTO createAlert(@RequestBody CreatePriceAlertRequest request) {
        return priceAlertService.createAlert(request);
    }

    @GetMapping
    public List<PriceAlertDTO> getAllAlerts() {
        return priceAlertService.getAllAlerts();
    }

    @GetMapping("/triggered")
    public List<PriceAlertDTO> getTriggeredAlerts() {
        return priceAlertService.getTriggeredAlerts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceAlertDTO> getAlertById(@PathVariable Long id) {
        return priceAlertService.getAlertById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}