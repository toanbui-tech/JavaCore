package com.javacore.ordermanagement.oop;

import com.javacore.ordermanagement.exception.InvalidOrderException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Order phoi hop nhieu class khac (Customer, OrderItem) -> the hien
 * COMPOSITION ("has-a"): mot Order CO mot Customer va CO NHIEU OrderItem,
 * khac voi quan he "is-a" cua ke thua.
 * <p>
 * Order cung IMPLEMENTS Comparable&lt;Order&gt; de co the sap xep/uu tien
 * trong hang doi (xem collections.OrderQueueManager dung PriorityQueue).
 */
public class Order implements Comparable<Order> {

    private final String id;
    private final Customer customer;
    private final List<OrderItem> items = new ArrayList<>();
    private final LocalDateTime createdAt;
    private OrderStatus status;

    public Order(String id, Customer customer) {
        this.id = Objects.requireNonNull(id);
        this.customer = Objects.requireNonNull(customer);
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    /** ENCAPSULATION: tra ve unmodifiable view -> ben ngoai khong the them/xoa truc tiep vao list noi bo. */
    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void changeStatus(OrderStatus newStatus) {
        if (status == OrderStatus.DELIVERED || status == OrderStatus.CANCELLED) {
            throw new InvalidOrderException(
                    "Khong the doi trang thai don '%s' vi don da o trang thai cuoi (%s)".formatted(id, status));
        }
        this.status = Objects.requireNonNull(newStatus);
    }

    public BigDecimal getSubtotal() {
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** POLYMORPHISM: goi customer.calculateDiscount() - ket qua phu thuoc vao loai Customer thuc su luc runtime. */
    public BigDecimal getTotal() {
        BigDecimal subtotal = getSubtotal();
        BigDecimal discount = customer.calculateDiscount(subtotal);
        return subtotal.subtract(discount);
    }

    /**
     * Thu tu uu tien: don cua khach VIP xu ly truoc, cung hang thi don tao
     * som hon xu ly truoc (FIFO trong cung nhom uu tien).
     */
    @Override
    public int compareTo(Order other) {
        boolean thisVip = "VIP".equals(this.customer.getMembershipTier());
        boolean otherVip = "VIP".equals(other.customer.getMembershipTier());
        if (thisVip != otherVip) {
            return thisVip ? -1 : 1;
        }
        return this.createdAt.compareTo(other.createdAt);
    }

    @Override
    public String toString() {
        return "Order{id='%s', customer='%s', status=%s, total=%s}"
                .formatted(id, customer.getName(), status, getTotal());
    }
}
