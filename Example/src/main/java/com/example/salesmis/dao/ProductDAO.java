package com.example.salesmis.dao;

import com.example.salesmis.model.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    List<Product> findAll();

    Optional<Product> findById(Long id);

    Optional<Product> findBySku(String sku);

    List<Product> searchByKeyword(String keyword);

    Product save(Product product);

    Product update(Product product);

    void deleteById(Long id);

    boolean existsBySku(String sku);
}
