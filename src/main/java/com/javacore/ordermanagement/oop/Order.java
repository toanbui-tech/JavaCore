package com.javacore.ordermanagement.oop;

import com.javacore.ordermanagement.exception.InvalidOrderException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Order phối hợp nhiều class khác (Customer, OrderItem) -> thể hiện
 * COMPOSITION ("has-a"): một Order CÓ một Customer và CÓ NHIỀU OrderItem,
 * khác với quan hệ "is-a" của kế thừa.
 * <p>
 * Order cũng IMPLEMENTS Comparable&lt;Order&gt; để có thể sắp xếp/ưu tiên
 * trong hàng đợi (xem collections.OrderQueueManager dùng PriorityQueue).
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

    /** ENCAPSULATION: trả về unmodifiable view -> bên ngoài không thể thêm/xóa trực tiếp vào list nội bộ. */
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

    /** POLYMORPHISM: gọi customer.calculateDiscount() - kết quả phụ thuộc vào loại Customer thực sự lúc runtime. */
    public BigDecimal getTotal() {
        BigDecimal subtotal = getSubtotal();
        BigDecimal discount = customer.calculateDiscount(subtotal);
        return subtotal.subtract(discount);
    }

    /**
     * Thứ tự ưu tiên: đơn của khách VIP xử lý trước, cùng hạng thì đơn tạo
     * sớm hơn xử lý trước (FIFO trong cùng nhóm ưu tiên).
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
