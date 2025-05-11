package com.example.demo.repo;

import com.example.demo.model.Product;
import com.example.demo.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByProductCategory(String category);
    List<Product> findByBrand(String brand);
    List<Product> findByStore(Store store);

    @Query("SELECT p FROM Product p WHERE p.productName = :name AND p.store.name != :storeName")
    List<Product> findSameProductInOtherStores(String name, String storeName);

    @Query("SELECT p FROM Product p WHERE p.productCategory = :category AND p.packageUnit = :unit")
    List<Product> findByProductCategoryAndPackageUnit(String category, String unit);
}