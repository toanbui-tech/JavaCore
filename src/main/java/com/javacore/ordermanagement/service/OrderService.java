package com.javacore.ordermanagement.service;

import com.javacore.ordermanagement.collections.OrderQueueManager;
import com.javacore.ordermanagement.exception.CustomerNotFoundException;
import com.javacore.ordermanagement.exception.InvalidOrderException;
import com.javacore.ordermanagement.exception.OutOfStockException;
import com.javacore.ordermanagement.exception.ProductNotFoundException;
import com.javacore.ordermanagement.generics.CustomerRepository;
import com.javacore.ordermanagement.generics.OrderRepository;
import com.javacore.ordermanagement.generics.ProductRepository;
import com.javacore.ordermanagement.oop.Customer;
import com.javacore.ordermanagement.oop.Order;
import com.javacore.ordermanagement.oop.OrderItem;
import com.javacore.ordermanagement.oop.OrderStatus;
import com.javacore.ordermanagement.oop.Product;
import com.javacore.ordermanagement.oop.payment.PaymentMethod;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * TANG SERVICE: noi phoi hop repository (generics), custom exception, va
 * collections lai voi nhau de thuc thi mot NGHIEP VU hoan chinh - dat mua
 * hang. Day la lop "duong dan chinh" (main flow) ma Main.java se goi de
 * chay demo, va cung la lop duoc unit test ky nhat (xem src/test).
 */
public class OrderService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderQueueManager queueManager;

    public OrderService(ProductRepository productRepository,
                         CustomerRepository customerRepository,
                         OrderRepository orderRepository,
                         OrderQueueManager queueManager) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.queueManager = queueManager;
    }

    /**
     * Dat mot don hang moi.
     *
     * @param customerId    id khach hang da ton tai
     * @param quantityByProductId map productId -> so luong muon mua
     * @throws OutOfStockException        (CHECKED) neu bat ky san pham nao khong du hang
     * @throws CustomerNotFoundException  (UNCHECKED) neu khong tim thay khach hang
     * @throws ProductNotFoundException   (UNCHECKED) neu khong tim thay san pham
     * @throws InvalidOrderException      (UNCHECKED) neu don hang khong co item nao
     */
    public Order placeOrder(String customerId, Map<String, Integer> quantityByProductId) throws OutOfStockException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        if (quantityByProductId.isEmpty()) {
            throw new InvalidOrderException("Don hang phai co it nhat mot san pham");
        }

        Order order = new Order(UUID.randomUUID().toString(), customer);

        for (Map.Entry<String, Integer> entry : quantityByProductId.entrySet()) {
            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new ProductNotFoundException(entry.getKey()));

            // Checked exception: buoc goi ham nay phai duoc bao boc trong try/catch
            // hoac lan truyen tiep bang "throws" - o day ta chon lan truyen len Main
            // de nguoi goi (Main) quyet dinh cach xu ly (thong bao, huy don...).
            product.reduceStock(entry.getValue());
            order.addItem(new OrderItem(product, entry.getValue()));
        }

        orderRepository.save(order);
        queueManager.enqueueByPriority(order);
        return order;
    }

    /** Xu ly don hang tiep theo trong hang doi uu tien (VIP truoc) va tien hanh thanh toan. */
    public Order processNextInQueue(PaymentMethod paymentMethod) {
        Order order = queueManager.pollByPriority();
        if (order == null) {
            return null;
        }
        return checkout(order, paymentMethod);
    }

    public Order checkout(Order order, PaymentMethod paymentMethod) {
        order.changeStatus(OrderStatus.PROCESSING);
        BigDecimal total = order.getTotal();

        // DA HINH: khong quan tam paymentMethod la Cash/CreditCard/EWallet cu the.
        boolean success = paymentMethod.pay(total);
        if (!success) {
            throw new InvalidOrderException(
                    "Thanh toan that bai cho don '%s' bang %s".formatted(order.getId(), paymentMethod.getMethodName()));
        }
        order.changeStatus(OrderStatus.SHIPPED);
        return order;
    }

    public void cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new InvalidOrderException("Khong tim thay don hang '%s'".formatted(orderId)));

        for (OrderItem item : order.getItems()) {
            item.getProduct().restock(item.getQuantity());
        }
        order.changeStatus(OrderStatus.CANCELLED);
    }
}
