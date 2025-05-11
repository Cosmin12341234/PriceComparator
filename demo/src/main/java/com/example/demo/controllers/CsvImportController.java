package com.example.demo.controllers;

import com.example.demo.service.CsvImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
@RestController
@RequestMapping("/api/import")
public class CsvImportController {

    private final CsvImportService csvImportService;

    public CsvImportController(CsvImportService csvImportService) {
        this.csvImportService = csvImportService;
    }

    @PostMapping("/auto")
    public ResponseEntity<String> importFile(@RequestParam("file") MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            if (filename == null) {
                return ResponseEntity.badRequest().body("Filename is required");
            }

            if (filename.contains("discount")) {
                String[] parts = filename.split("_");
                if (parts.length < 3) {
                    return ResponseEntity.badRequest().body("Invalid discount filename format. Expected: storename_discount_date.csv");
                }
                String storeName = parts[0];
                csvImportService.importDiscountsFromCsv(file, storeName);
                return ResponseEntity.ok("Discounts imported successfully for store: " + storeName);
            } else {
                String[] parts = filename.split("_");
                if (parts.length < 2) {
                    return ResponseEntity.badRequest().body("Invalid product filename format. Expected: storename_date.csv");
                }

                String storeName = parts[0];
                String dateStr = parts[1].replace(".csv", "");
                LocalDate date = LocalDate.parse(dateStr);

                csvImportService.importProductsFromCsv(file, storeName, date);
                return ResponseEntity.ok("Products imported successfully for store: " + storeName + ", date: " + dateStr);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error importing file: " + e.getMessage());
        }
    }
}