package com.javacore.ordermanagement.stream;

import com.javacore.ordermanagement.oop.Order;
import com.javacore.ordermanagement.oop.OrderItem;
import com.javacore.ordermanagement.oop.OrderStatus;
import com.javacore.ordermanagement.oop.Product;
import com.javacore.ordermanagement.oop.RegularCustomer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ReportServiceTest {

    private final ReportService reportService = new ReportService();

    private Product laptop;
    private Product mouse;
    private Order order1;
    private Order order2;
    private Order cancelledOrder;

    @BeforeEach
    void setUp() {
        laptop = new Product("P1", "Laptop", "Electronics", new BigDecimal("1000.00"), 10);
        mouse = new Product("P2", "Mouse", "Electronics", new BigDecimal("20.00"), 10);

        RegularCustomer bob = new RegularCustomer("C1", "Bob", "bob@example.com");

        order1 = new Order("O1", bob);
        order1.addItem(new OrderItem(laptop, 1));
        order1.addItem(new OrderItem(mouse, 2));

        order2 = new Order("O2", bob);
        order2.addItem(new OrderItem(mouse, 1));

        cancelledOrder = new Order("O3", bob);
        cancelledOrder.addItem(new OrderItem(laptop, 5));
        cancelledOrder.changeStatus(OrderStatus.CANCELLED);
    }

    @Test
    void totalRevenue_shouldExcludeCancelledOrders() {
        BigDecimal total = reportService.totalRevenue(List.of(order1, order2, cancelledOrder));

        // order1 = 1000 + 40 = 1040 ; order2 = 20 ; cancelledOrder bị loại ra
        assertEquals(0, new BigDecimal("1060.00").compareTo(total));
    }

    @Test
    void topSellingProducts_shouldRankByTotalQuantitySold() {
        List<Map.Entry<Product, Long>> topProducts = reportService.topSellingProducts(List.of(order1, order2), 1);

        assertFalse(topProducts.isEmpty());
        assertEquals(mouse, topProducts.get(0).getKey()); // mouse: 2 + 1 = 3 > laptop: 1
        assertEquals(3L, topProducts.get(0).getValue());
    }

    @Test
    void groupOrdersByStatus_shouldGroupCorrectly() {
        Map<OrderStatus, List<Order>> grouped = reportService.groupOrdersByStatus(List.of(order1, order2, cancelledOrder));

        assertEquals(2, grouped.get(OrderStatus.PENDING).size());
        assertEquals(1, grouped.get(OrderStatus.CANCELLED).size());
    }
}
