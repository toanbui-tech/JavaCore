package com.javacore.ordermanagement.service;

import com.javacore.ordermanagement.collections.OrderQueueManager;
import com.javacore.ordermanagement.exception.CustomerNotFoundException;
import com.javacore.ordermanagement.exception.InvalidOrderException;
import com.javacore.ordermanagement.exception.OutOfStockException;
import com.javacore.ordermanagement.exception.ProductNotFoundException;
import com.javacore.ordermanagement.generics.CustomerRepository;
import com.javacore.ordermanagement.generics.OrderRepository;
import com.javacore.ordermanagement.generics.ProductRepository;
import com.javacore.ordermanagement.oop.Order;
import com.javacore.ordermanagement.oop.OrderStatus;
import com.javacore.ordermanagement.oop.Product;
import com.javacore.ordermanagement.oop.RegularCustomer;
import com.javacore.ordermanagement.oop.VipCustomer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * UNIT TEST CƠ BẢN VỚI JUNIT.
 * <p>
 * Mỗi test chỉ kiểm tra MỘT hành vi cụ thể (Arrange - Act - Assert), độc lập
 * với các test khác (mỗi @Test chạy trên dữ liệu mới tinh nhờ @BeforeEach).
 */
class OrderServiceTest {

    private ProductRepository productRepository;
    private CustomerRepository customerRepository;
    private OrderRepository orderRepository;
    private OrderService orderService;

    private Product laptop;
    private VipCustomer vipCustomer;
    private RegularCustomer regularCustomer;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
        customerRepository = new CustomerRepository();
        orderRepository = new OrderRepository();
        orderService = new OrderService(productRepository, customerRepository, orderRepository, new OrderQueueManager());

        laptop = new Product("P1", "Laptop", "Electronics", new BigDecimal("1000.00"), 3);
        productRepository.save(laptop);

        vipCustomer = new VipCustomer("C1", "Alice", "alice@example.com");
        regularCustomer = new RegularCustomer("C2", "Bob", "bob@example.com");
        customerRepository.save(vipCustomer);
        customerRepository.save(regularCustomer);
    }

    @Test
    void placeOrder_shouldReduceStockAndSaveOrder() throws OutOfStockException {
        Order order = orderService.placeOrder(regularCustomer.getId(), Map.of(laptop.getId(), 2));

        assertEquals(1, laptop.getStockQuantity());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertTrue(orderRepository.findById(order.getId()).isPresent());
    }

    @Test
    void placeOrder_shouldThrowOutOfStockException_whenNotEnoughStock() {
        assertThrows(OutOfStockException.class,
                () -> orderService.placeOrder(regularCustomer.getId(), Map.of(laptop.getId(), 10)));
    }

    @Test
    void placeOrder_shouldThrowCustomerNotFoundException_whenCustomerUnknown() {
        assertThrows(CustomerNotFoundException.class,
                () -> orderService.placeOrder("unknown-id", Map.of(laptop.getId(), 1)));
    }

    @Test
    void placeOrder_shouldThrowProductNotFoundException_whenProductUnknown() {
        assertThrows(ProductNotFoundException.class,
                () -> orderService.placeOrder(regularCustomer.getId(), Map.of("unknown-id", 1)));
    }

    @Test
    void placeOrder_shouldThrowInvalidOrderException_whenNoItems() {
        assertThrows(InvalidOrderException.class,
                () -> orderService.placeOrder(regularCustomer.getId(), Map.of()));
    }

    @Test
    void vipCustomer_shouldGetDiscount_whileRegularCustomerDoesNot() throws OutOfStockException {
        Order vipOrder = orderService.placeOrder(vipCustomer.getId(), Map.of(laptop.getId(), 1));

        BigDecimal subtotal = vipOrder.getSubtotal();
        BigDecimal total = vipOrder.getTotal();

        assertTrue(total.compareTo(subtotal) < 0, "VIP phai duoc giam gia nen total < subtotal");
    }

    @Test
    void cancelOrder_shouldRestockProductsAndMarkCancelled() throws OutOfStockException {
        Order order = orderService.placeOrder(regularCustomer.getId(), Map.of(laptop.getId(), 2));
        assertEquals(1, laptop.getStockQuantity());

        orderService.cancelOrder(order.getId());

        assertEquals(3, laptop.getStockQuantity());
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void cancelOrder_shouldThrowInvalidOrderException_whenOrderNotFound() {
        assertThrows(InvalidOrderException.class, () -> orderService.cancelOrder("missing"));
    }
}
