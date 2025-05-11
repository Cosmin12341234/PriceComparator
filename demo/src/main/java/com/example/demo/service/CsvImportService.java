package com.example.demo.service;

import com.example.demo.model.Discount;
import com.example.demo.model.Product;
import com.example.demo.model.Store;
import com.example.demo.repo.DiscountRepository;
import com.example.demo.repo.ProductRepository;
import com.example.demo.repo.StoreRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvImportService {

    private final ProductRepository productRepository;

    private final StoreRepository storeRepository;

    private final DiscountRepository discountRepository;

    private final PriceHistoryService priceHistoryService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CsvImportService(ProductRepository productRepository, StoreRepository storeRepository, DiscountRepository discountRepository, PriceHistoryService priceHistoryService) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.discountRepository = discountRepository;
        this.priceHistoryService = priceHistoryService;
    }

    public void importProductsFromCsv(MultipartFile file, String storeName, LocalDate date) throws IOException {

        Store store = getOrCreateStore(storeName);

        List<Product> products = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                Product product = new Product();
                product.setProductId(csvRecord.get("product_id"));
                product.setProductName(csvRecord.get("product_name"));
                product.setProductCategory(csvRecord.get("product_category"));
                product.setBrand(csvRecord.get("brand"));
                product.setPackageQuantity(Double.parseDouble(csvRecord.get("package_quantity")));
                product.setPackageUnit(csvRecord.get("package_unit"));
                product.setPrice(new BigDecimal(csvRecord.get("price")));
                product.setCurrency(csvRecord.get("currency"));
                product.setStore(store);

                products.add(product);
            }
            productRepository.saveAll(products);

            products.forEach(product -> priceHistoryService.recordPrice(product, date));
        }
    }

    public void importDiscountsFromCsv(MultipartFile file, String storeName) throws IOException {
        Store store = getOrCreateStore(storeName);

        List<Discount> discounts = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                String productId = csvRecord.get("product_id");

                Product product = productRepository.findById(productId).orElseGet(() -> {
                    Product p = new Product();
                    p.setProductId(productId);
                    p.setProductName(csvRecord.get("product_name"));
                    p.setBrand(csvRecord.get("brand"));
                    p.setPackageQuantity(Double.parseDouble(csvRecord.get("package_quantity")));
                    p.setPackageUnit(csvRecord.get("package_unit"));
                    p.setProductCategory(csvRecord.get("product_category"));
                    p.setStore(store);
                    p.setPrice(BigDecimal.ZERO);
                    p.setCurrency("RON");
                    return productRepository.save(p);
                });

                Discount discount = new Discount();
                discount.setProduct(product);
                discount.setFromDate(LocalDate.parse(csvRecord.get("from_date"), DATE_FORMATTER));
                discount.setToDate(LocalDate.parse(csvRecord.get("to_date"), DATE_FORMATTER));
                discount.setPercentageOfDiscount(Double.parseDouble(csvRecord.get("percentage_of_discount")));
                discount.setCreatedDate(LocalDate.now());

                discounts.add(discount);
            }
            discountRepository.saveAll(discounts);
        }
    }

    private Store getOrCreateStore(String storeName) {
        return storeRepository.findById(storeName)
                .orElseGet(() -> {
                    Store newStore = new Store();
                    newStore.setName(storeName);
                    return storeRepository.save(newStore);
                });
    }
}