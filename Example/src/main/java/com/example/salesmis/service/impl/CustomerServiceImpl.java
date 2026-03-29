package com.example.salesmis.service.impl;

import com.example.salesmis.dao.CustomerDAO;
import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerServiceImpl(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }

    @Override
    public List<Customer> searchCustomers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return customerDAO.findAll();
        }
        return customerDAO.searchByKeyword(keyword.trim());
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với id = " + id));
    }

    @Override
    public Customer createCustomer(String customerCode,
                                   String fullName,
                                   String phone,
                                   String email,
                                   String address,
                                   Boolean active) {

        validate(customerCode, fullName, phone, email, address);

        if (customerDAO.existsByCode(customerCode.trim())) {
            throw new IllegalArgumentException("Mã khách hàng đã tồn tại: " + customerCode);
        }

        if (email != null && !email.trim().isEmpty() && customerDAO.existsByEmail(email.trim())) {
            throw new IllegalArgumentException("Email đã tồn tại: " + email);
        }

        Customer customer = new Customer();
        customer.setCustomerCode(customerCode.trim());
        customer.setFullName(fullName.trim());
        customer.setPhone(normalize(phone));
        customer.setEmail(normalize(email));
        customer.setAddress(normalize(address));
        customer.setActive(active != null ? active : Boolean.TRUE);

        return customerDAO.save(customer);
    }

    @Override
    public Customer updateCustomer(Long id,
                                   String customerCode,
                                   String fullName,
                                   String phone,
                                   String email,
                                   String address,
                                   Boolean active) {

        validate(customerCode, fullName, phone, email, address);

        Customer existing = customerDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với id = " + id));

        Customer sameCode = customerDAO.findByCode(customerCode.trim()).orElse(null);
        if (sameCode != null && !sameCode.getId().equals(id)) {
            throw new IllegalArgumentException("Mã khách hàng đã được dùng bởi khách hàng khác.");
        }

        if (email != null && !email.trim().isEmpty()) {
            Customer sameEmail = customerDAO.findByEmail(email.trim()).orElse(null);
            if (sameEmail != null && !sameEmail.getId().equals(id)) {
                throw new IllegalArgumentException("Email đã được dùng bởi khách hàng khác.");
            }
        }

        existing.setCustomerCode(customerCode.trim());
        existing.setFullName(fullName.trim());
        existing.setPhone(normalize(phone));
        existing.setEmail(normalize(email));
        existing.setAddress(normalize(address));
        existing.setActive(active != null ? active : Boolean.TRUE);

        return customerDAO.update(existing);
    }

    @Override
    public void deleteCustomer(Long id) {
        getCustomerById(id);
        customerDAO.deleteById(id);
    }

    private void validate(String customerCode,
                          String fullName,
                          String phone,
                          String email,
                          String address) {

        if (customerCode == null || customerCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khách hàng không được để trống.");
        }

        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng không được để trống.");
        }

        if (customerCode.trim().length() > 20) {
            throw new IllegalArgumentException("Mã khách hàng tối đa 20 ký tự.");
        }

        if (fullName.trim().length() > 150) {
            throw new IllegalArgumentException("Tên khách hàng tối đa 150 ký tự.");
        }

        if (phone != null && phone.trim().length() > 20) {
            throw new IllegalArgumentException("Số điện thoại tối đa 20 ký tự.");
        }

        if (email != null && email.trim().length() > 150) {
            throw new IllegalArgumentException("Email tối đa 150 ký tự.");
        }

        if (address != null && address.trim().length() > 255) {
            throw new IllegalArgumentException("Địa chỉ tối đa 255 ký tự.");
        }

        if (email != null && !email.trim().isEmpty() && !isValidEmail(email.trim())) {
            throw new IllegalArgumentException("Email không đúng định dạng.");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private String normalize(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }
}
