package com.javacore.ordermanagement.concurrency;

import com.javacore.ordermanagement.oop.Order;
import com.javacore.ordermanagement.oop.payment.PaymentMethod;
import com.javacore.ordermanagement.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * ĐA LUỒNG - ExecutorService + Callable + Future.
 * <p>
 * Tình huống thực tế: cuối ngày hệ thống cần "chốt đơn" (checkout) hàng loạt
 * đơn đang PENDING cùng lúc, thay vì xử lý tuần tự từng đơn một. Dùng một
 * THREAD POOL cố định để giới hạn số lượng thread chạy song song (tránh tạo
 * quá nhiều thread làm quá tải hệ thống), và Callable&lt;Order&gt; (khác
 * Runnable ở chỗ CÓ GIÁ TRỊ TRẢ VỀ và có thể ném checked exception).
 * <p>
 * THREAD-SAFETY: nhiều thread có thể cùng reduceStock()/restock() trên CÙNG
 * MỘT Product (ví dụ 2 đơn khác nhau mua chung 1 sản phẩm) - do đó
 * Product#reduceStock/restock được khai báo synchronized (xem Product.java)
 * để tránh "lost update" (2 thread cùng đọc giá trị tồn kho cũ, cùng ghi đè
 * lên nhau).
 */
public class OrderBatchProcessor {

    private final OrderService orderService;

    public OrderBatchProcessor(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Xử lý (checkout) danh sách đơn hàng SONG SONG bằng một thread pool có
     * kích thước cố định, chờ tới khi tất cả hoàn tất rồi trả về kết quả.
     */
    public List<Order> processBatch(List<Order> orders, PaymentMethod paymentMethod, int threadPoolSize) {
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        List<Order> processedOrders = new ArrayList<>();
        try {
            List<Callable<Order>> tasks = orders.stream()
                    .<Callable<Order>>map(order -> () -> orderService.checkout(order, paymentMethod))
                    .toList();

            // invokeAll: gửi tất cả task vào pool, block cho đến khi TẤT CẢ hoàn tất.
            List<Future<Order>> futures = executor.invokeAll(tasks);

            for (Future<Order> future : futures) {
                processedOrders.add(future.get());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException("Loi khi xu ly don hang trong thread pool", e.getCause());
        } finally {
            shutdown(executor);
        }
        return processedOrders;
    }

    private void shutdown(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
