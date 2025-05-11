package com.example.demo.repo;


import com.example.demo.model.Discount;
import com.example.demo.model.Product;
import com.example.demo.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    @Query("SELECT d FROM Discount d WHERE d.fromDate <= :today AND d.toDate >= :today")
    List<Discount> findActiveDiscounts(LocalDate today);

    @Query("SELECT d FROM Discount d WHERE d.createdDate = :date")
    List<Discount> findNewDiscounts(LocalDate date);
}