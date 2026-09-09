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
 * TẦNG SERVICE: nơi phối hợp repository (generics), custom exception, và
 * collections lại với nhau để thực thi một NGHIỆP VỤ hoàn chỉnh - đặt mua
 * hàng. Đây là lớp "đường dẫn chính" (main flow) mà Main.java sẽ gọi để
 * chạy demo, và cũng là lớp được unit test kỹ nhất (xem src/test).
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
     * Đặt một đơn hàng mới.
     *
     * @param customerId    id khách hàng đã tồn tại
     * @param quantityByProductId map productId -> số lượng muốn mua
     * @throws OutOfStockException        (CHECKED) nếu bất kỳ sản phẩm nào không đủ hàng
     * @throws CustomerNotFoundException  (UNCHECKED) nếu không tìm thấy khách hàng
     * @throws ProductNotFoundException   (UNCHECKED) nếu không tìm thấy sản phẩm
     * @throws InvalidOrderException      (UNCHECKED) nếu đơn hàng không có item nào
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

            // Checked exception: buộc gọi hàm này phải được bao bọc trong try/catch
            // hoặc lan truyền tiếp bằng "throws" - ở đây ta chọn lan truyền lên Main
            // để người gọi (Main) quyết định cách xử lý (thông báo, hủy đơn...).
            product.reduceStock(entry.getValue());
            order.addItem(new OrderItem(product, entry.getValue()));
        }

        orderRepository.save(order);
        queueManager.enqueueByPriority(order);
        return order;
    }

    /** Xử lý đơn hàng tiếp theo trong hàng đợi ưu tiên (VIP trước) và tiến hành thanh toán. */
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

        // ĐA HÌNH: không quan tâm paymentMethod là Cash/CreditCard/EWallet cụ thể.
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
