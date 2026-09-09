package com.javacore.ordermanagement;

import com.javacore.ordermanagement.collections.OrderQueueManager;
import com.javacore.ordermanagement.collections.ProductCatalog;
import com.javacore.ordermanagement.concurrency.NotificationSender;
import com.javacore.ordermanagement.concurrency.OrderBatchProcessor;
import com.javacore.ordermanagement.exception.InvalidOrderException;
import com.javacore.ordermanagement.exception.OutOfStockException;
import com.javacore.ordermanagement.generics.CustomerRepository;
import com.javacore.ordermanagement.generics.OrderRepository;
import com.javacore.ordermanagement.generics.ProductRepository;
import com.javacore.ordermanagement.io.CsvExporter;
import com.javacore.ordermanagement.io.JsonExporter;
import com.javacore.ordermanagement.oop.Customer;
import com.javacore.ordermanagement.oop.Order;
import com.javacore.ordermanagement.oop.Product;
import com.javacore.ordermanagement.oop.RegularCustomer;
import com.javacore.ordermanagement.oop.VipCustomer;
import com.javacore.ordermanagement.oop.payment.CashPayment;
import com.javacore.ordermanagement.oop.payment.CreditCardPayment;
import com.javacore.ordermanagement.oop.payment.PaymentMethod;
import com.javacore.ordermanagement.service.OrderService;
import com.javacore.ordermanagement.stream.ReportService;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ĐIỂM VÀO CHƯƠNG TRÌNH: chạy tuần tự qua toàn bộ hệ thống Quản lý đơn hàng
 * để minh họa mọi khái niệm Java Core làm việc CÙNG NHAU trong một bối cảnh
 * thực tế, thay vì các ví dụ rời rạc.
 * <p>
 * Nếu bạn mới đọc project này lần đầu, xem README.md ở thư mục gốc để biết
 * lộ trình nên đọc/code theo thứ tự nào.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        // 1) Khởi tạo hạ tầng: repository (generics) + collections chuyên biệt
        ProductRepository productRepository = new ProductRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        OrderRepository orderRepository = new OrderRepository();
        ProductCatalog catalog = new ProductCatalog();
        OrderQueueManager queueManager = new OrderQueueManager();
        OrderService orderService = new OrderService(productRepository, customerRepository, orderRepository, queueManager);
        ReportService reportService = new ReportService();
        NotificationSender notificationSender = new NotificationSender();

        // 2) Dữ liệu mẫu: sản phẩm
        Product laptop = new Product("P1", "Laptop Pro 14", "Electronics", new BigDecimal("1500.00"), 5);
        Product mouse = new Product("P2", "Wireless Mouse", "Electronics", new BigDecimal("25.00"), 2);
        Product desk = new Product("P3", "Standing Desk", "Furniture", new BigDecimal("300.00"), 10);
        for (Product p : List.of(laptop, mouse, desk)) {
            productRepository.save(p);
            catalog.add(p);
        }
        System.out.println("Danh muc san pham: " + catalog.getCategories());

        // 3) Dữ liệu mẫu: khách hàng - RegularCustomer/VipCustomer minh họa đa hình
        Customer alice = customerRepository.save(new VipCustomer("C1", "Alice", "alice@example.com"));
        Customer bob = customerRepository.save(new RegularCustomer("C2", "Bob", "bob@example.com"));

        // 4) Đặt hàng - minh họa CHECKED EXCEPTION (OutOfStockException) phải được xử lý
        try {
            Order aliceOrder = orderService.placeOrder(alice.getId(), Map.of(laptop.getId(), 1, mouse.getId(), 1));
            System.out.println("Da tao don hang: " + aliceOrder);
        } catch (OutOfStockException e) {
            System.out.println("Khong the dat hang: " + e.getMessage());
        }

        try {
            // Bob mua 5 con chuột trong khi kho chỉ còn 1 -> sẽ ném OutOfStockException
            orderService.placeOrder(bob.getId(), Map.of(mouse.getId(), 5));
        } catch (OutOfStockException e) {
            System.out.println("Bat duoc loi nghiep vu du kien: " + e.getMessage());
        }

        try {
            Order bobOrder = orderService.placeOrder(bob.getId(), Map.of(desk.getId(), 2));
            System.out.println("Da tao don hang: " + bobOrder);
        } catch (OutOfStockException e) {
            System.out.println("Khong the dat hang: " + e.getMessage());
        }

        // 5) Xử lý hàng đợi ưu tiên: đơn của VIP (Alice) được xử lý trước đơn của Bob
        System.out.println("\n-- Xu ly hang doi uu tien (VIP truoc) --");
        PaymentMethod cash = new CashPayment();
        Order processed;
        while ((processed = orderService.processNextInQueue(cash)) != null) {
            System.out.println("Da xu ly: " + processed);
            notificationSender.sendAsync(processed.getCustomer().getEmail(),
                    "Don hang " + processed.getId() + " da duoc giao cho don vi van chuyen.");
        }

        // 6) Minh họa UNCHECKED EXCEPTION: hủy một đơn không tồn tại
        try {
            orderService.cancelOrder("khong-ton-tai");
        } catch (InvalidOrderException e) {
            System.out.println("\nBat duoc loi du kien: " + e.getMessage());
        }

        // 7) Báo cáo thống kê bằng Stream API
        List<Order> allOrders = orderRepository.findAll();
        System.out.println("\n-- Bao cao (Stream API) --");
        System.out.println("Tong doanh thu: " + reportService.totalRevenue(allOrders));
        System.out.println("Gia tri trung binh/don: " + reportService.averageOrderValue(allOrders));
        System.out.println("Don hang theo trang thai: " + reportService.groupOrdersByStatus(allOrders).keySet());
        System.out.println("Doanh thu theo khach hang: " + reportService.revenueByCustomerName(allOrders));
        reportService.topSellingProducts(allOrders, 3)
                .forEach(entry -> System.out.printf("  Ban chay: %s - %d san pham%n", entry.getKey().getName(), entry.getValue()));

        // 8) Xử lý đa luồng: đặt thêm vài đơn PENDING rồi checkout song song bằng ExecutorService
        System.out.println("\n-- Xu ly hang loat da luong (ExecutorService) --");
        try {
            orderService.placeOrder(alice.getId(), Map.of(laptop.getId(), 1));
            orderService.placeOrder(bob.getId(), Map.of(desk.getId(), 1));
        } catch (OutOfStockException e) {
            System.out.println("Khong the tao don cho demo da luong: " + e.getMessage());
        }
        // Các đơn được đặt qua placeOrder() đã tự động vào priority queue; lấy ra để xử lý song song
        List<Order> toProcess = new ArrayList<>();
        Order next;
        while ((next = queueManager.pollByPriority()) != null) {
            toProcess.add(next);
        }
        OrderBatchProcessor batchProcessor = new OrderBatchProcessor(orderService);
        PaymentMethod creditCard = new CreditCardPayment("**** 1234", new BigDecimal("100000.00"));
        List<Order> results = batchProcessor.processBatch(toProcess, creditCard, 4);
        results.forEach(order -> System.out.println("Da checkout song song: " + order));

        // 9) Xuất dữ liệu ra file (I/O)
        Path outputDir = Path.of("output");
        Files.createDirectories(outputDir);
        new CsvExporter().exportOrders(orderRepository.findAll(), outputDir.resolve("orders.csv"));
        new JsonExporter().exportProducts(productRepository.findAll(), outputDir.resolve("products.json"));
        System.out.println("\nDa xuat file: output/orders.csv, output/products.json");
    }
}
