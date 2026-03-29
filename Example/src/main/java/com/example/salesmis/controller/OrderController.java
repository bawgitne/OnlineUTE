package com.example.salesmis.controller;

import com.example.salesmis.model.dto.OrderLineInput;
import com.example.salesmis.model.entity.Customer;
import com.example.salesmis.model.entity.Product;
import com.example.salesmis.model.entity.SalesOrder;
import com.example.salesmis.model.enumtype.OrderStatus;
import com.example.salesmis.service.LookupService;
import com.example.salesmis.service.OrderService;

import java.time.LocalDate;
import java.util.List;

public class OrderController {
    private final OrderService orderService;
    private final LookupService lookupService;

    public OrderController(OrderService orderService, LookupService lookupService) {
        this.orderService = orderService;
        this.lookupService = lookupService;
    }

    public List<SalesOrder> getAllOrders() { return orderService.getAllOrders(); }
    public List<SalesOrder> searchOrders(String keyword) { return orderService.searchOrders(keyword); }
    public SalesOrder getOrderById(Long id) { return orderService.getOrderById(id); }

    public List<Customer> getLookupCustomers() { return lookupService.getAllActiveCustomers(); }
    public List<Product> getLookupProducts() { return lookupService.getAllActiveProducts(); }

    public SalesOrder createOrder(String orderNo, LocalDate orderDate, Long customerId,
                                  OrderStatus status, String note, List<OrderLineInput> lines) {
        return orderService.createOrder(orderNo, orderDate, customerId, status, note, lines);
    }

    public SalesOrder updateOrder(Long id, String orderNo, LocalDate orderDate, Long customerId,
                                  OrderStatus status, String note, List<OrderLineInput> lines) {
        return orderService.updateOrder(id, orderNo, orderDate, customerId, status, note, lines);
    }

    public void deleteOrder(Long id) { orderService.deleteOrder(id); }
}
