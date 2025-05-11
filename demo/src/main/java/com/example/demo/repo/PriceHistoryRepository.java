package com.example.demo.repo;

import com.example.demo.model.PriceHistory;
import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    List<PriceHistory> findByProductOrderByDateAsc(Product product);
    List<PriceHistory> findByProductAndDateBetweenOrderByDateAsc(Product product, LocalDate start, LocalDate end);
}