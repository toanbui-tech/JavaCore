package com.javacore.ordermanagement.generics;

import com.javacore.ordermanagement.oop.Order;
import com.javacore.ordermanagement.oop.OrderStatus;

import java.util.List;

public class OrderRepository extends AbstractInMemoryRepository<Order, String> {

    public OrderRepository() {
        super(Order::getId);
    }

    public List<Order> findByStatus(OrderStatus status) {
        return findAll().stream()
                .filter(order -> order.getStatus() == status)
                .toList();
    }

    public List<Order> findByCustomerId(String customerId) {
        return findAll().stream()
                .filter(order -> order.getCustomer().getId().equals(customerId))
                .toList();
    }
}
