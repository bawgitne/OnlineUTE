package com.example.salesmis.service;

import com.example.salesmis.model.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    List<Product> searchProducts(String keyword);

    Product getProductById(Long id);

    Product createProduct(String sku,
                          String productName,
                          String category,
                          BigDecimal unitPrice,
                          Integer stockQty,
                          Boolean active);

    Product updateProduct(Long id,
                          String sku,
                          String productName,
                          String category,
                          BigDecimal unitPrice,
                          Integer stockQty,
                          Boolean active);

    void deleteProduct(Long id);
}
