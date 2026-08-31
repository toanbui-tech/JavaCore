package com.javacore.ordermanagement.stream;

import com.javacore.ordermanagement.oop.Order;
import com.javacore.ordermanagement.oop.OrderItem;
import com.javacore.ordermanagement.oop.OrderStatus;
import com.javacore.ordermanagement.oop.Product;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JAVA STREAM API & LAMBDA EXPRESSION.
 * <p>
 * Thay vi viet vong for/if long dong de tinh toan/thong ke thu cong, Stream
 * API mo ta "CAI GI can lam" (filter, map, group, reduce...) thay vi "LAM
 * THE NAO" (khai bao bien tam, tang chi so...) - code ngan gon, khai bao
 * (declarative) va de doc hon.
 * <p>
 * Luu y: cac phuong thuc o day KHONG thay doi (immutable) danh sach dau vao,
 * chi doc va tra ve ket qua moi - dung tinh chat "khong tac dung phu" ma
 * Stream khuyen khich.
 */
public class ReportService {

    /** Tong doanh thu = tong Order#getTotal() cua cac don da hoan tat (khong tinh don huy). */
    public BigDecimal totalRevenue(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Gom nhom don hang theo trang thai - Map<OrderStatus, List<Order>>. */
    public Map<OrderStatus, List<Order>> groupOrdersByStatus(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getStatus));
    }

    /** Doanh thu theo tung khach hang (ten khach -> tong tien da chi, cac don da huy khong tinh). */
    public Map<String, BigDecimal> revenueByCustomerName(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .collect(Collectors.groupingBy(
                        order -> order.getCustomer().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Order::getTotal, BigDecimal::add)));
    }

    /** Top N san pham ban chay nhat theo tong SO LUONG da ban, sap xep giam dan. */
    public List<Map.Entry<Product, Long>> topSellingProducts(List<Order> orders, int topN) {
        Map<Product, Long> soldQuantityByProduct = orders.stream()
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(OrderItem::getProduct, Collectors.summingLong(OrderItem::getQuantity)));

        return soldQuantityByProduct.entrySet().stream()
                .sorted(Map.Entry.<Product, Long>comparingByValue().reversed())
                .limit(topN)
                .toList();
    }

    /** Gia tri trung binh moi don hang (khong tinh don huy). */
    public BigDecimal averageOrderValue(List<Order> orders) {
        List<Order> validOrders = orders.stream()
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .toList();
        if (validOrders.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = totalRevenue(validOrders);
        return total.divide(BigDecimal.valueOf(validOrders.size()), 2, java.math.RoundingMode.HALF_UP);
    }

    /** Danh sach don hang co gia tri lon nhat, sap xep giam dan - vi du dung Comparator + lambda. */
    public List<Order> topOrdersByValue(List<Order> orders, int topN) {
        return orders.stream()
                .sorted(Comparator.comparing(Order::getTotal).reversed())
                .limit(topN)
                .toList();
    }
}
