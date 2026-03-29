package com.example.salesmis.controller;

import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.service.CustomerService;

import java.util.List;

public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public List<Customer> searchCustomers(String keyword) {
        return customerService.searchCustomers(keyword);
    }

    public Customer getCustomerById(Long id) {
        return customerService.getCustomerById(id);
    }

    public Customer createCustomer(String customerCode,
                                   String fullName,
                                   String phone,
                                   String email,
                                   String address,
                                   boolean active) {

        return customerService.createCustomer(
                customerCode,
                fullName,
                phone,
                email,
                address,
                active
        );
    }

    public Customer updateCustomer(Long id,
                                   String customerCode,
                                   String fullName,
                                   String phone,
                                   String email,
                                   String address,
                                   boolean active) {

        return customerService.updateCustomer(
                id,
                customerCode,
                fullName,
                phone,
                email,
                address,
                active
        );
    }

    public void deleteCustomer(Long id) {
        customerService.deleteCustomer(id);
    }
}
