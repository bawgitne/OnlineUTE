package com.example.salesmis.dao;

import com.example.salesmis.model.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    List<Customer> findAll();

    Optional<Customer> findById(Long id);

    Optional<Customer> findByCode(String customerCode);

    Optional<Customer> findByEmail(String email);

    List<Customer> searchByKeyword(String keyword);

    Customer save(Customer customer);

    Customer update(Customer customer);

    void deleteById(Long id);

    boolean existsByCode(String customerCode);

    boolean existsByEmail(String email);
}
