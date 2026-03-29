package com.example.salesmis.service;

import com.example.salesmis.model.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();

    List<Customer> searchCustomers(String keyword);

    Customer getCustomerById(Long id);

    Customer createCustomer(String customerCode,
                            String fullName,
                            String phone,
                            String email,
                            String address,
                            Boolean active);

    Customer updateCustomer(Long id,
                            String customerCode,
                            String fullName,
                            String phone,
                            String email,
                            String address,
                            Boolean active);

    void deleteCustomer(Long id);
}
