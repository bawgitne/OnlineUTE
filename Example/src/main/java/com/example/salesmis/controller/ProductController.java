package com.example.salesmis.controller;

import com.example.salesmis.model.entity.Product;
import com.example.salesmis.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    public List<Product> searchProducts(String keyword) {
        return productService.searchProducts(keyword);
    }

    public Product getProductById(Long id) {
        return productService.getProductById(id);
    }

    public Product createProduct(String sku,
                                 String productName,
                                 String category,
                                 String unitPriceText,
                                 String stockQtyText,
                                 boolean active) {

        BigDecimal unitPrice = parseBigDecimal(unitPriceText, "Đơn giá");
        Integer stockQty = parseInteger(stockQtyText, "Tồn kho");

        return productService.createProduct(
                sku,
                productName,
                category,
                unitPrice,
                stockQty,
                active
        );
    }

    public Product updateProduct(Long id,
                                 String sku,
                                 String productName,
                                 String category,
                                 String unitPriceText,
                                 String stockQtyText,
                                 boolean active) {

        BigDecimal unitPrice = parseBigDecimal(unitPriceText, "Đơn giá");
        Integer stockQty = parseInteger(stockQtyText, "Tồn kho");

        return productService.updateProduct(
                id,
                sku,
                productName,
                category,
                unitPrice,
                stockQty,
                active
        );
    }

    public void deleteProduct(Long id) {
        productService.deleteProduct(id);
    }

    private Integer parseInteger(String value, String fieldName) {
        try {
            return Integer.valueOf(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(fieldName + " phải là số nguyên hợp lệ.");
        }
    }

    private BigDecimal parseBigDecimal(String value, String fieldName) {
        try {
            return new BigDecimal(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(fieldName + " phải là số hợp lệ.");
        }
    }
}
