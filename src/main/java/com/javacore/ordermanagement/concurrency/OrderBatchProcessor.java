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
 * DA LUONG - ExecutorService + Callable + Future.
 * <p>
 * Tinh huong thuc te: cuoi ngay he thong can "chot don" (checkout) hang loat
 * don dang PENDING cung luc, thay vi xu ly tuan tu tung don mot. Dung mot
 * THREAD POOL co dinh de gioi han so luong thread chay song song (tranh tao
 * qua nhieu thread lam qua tai he thong), va Callable&lt;Order&gt; (khac
 * Runnable o cho CO GIA TRI TRA VE va co the nem checked exception).
 * <p>
 * THREAD-SAFETY: nhieu thread co the cung reduceStock()/restock() tren CUNG
 * MOT Product (vi du 2 don khac nhau mua chung 1 san pham) - do do
 * Product#reduceStock/restock duoc khai bao synchronized (xem Product.java)
 * de tranh "lost update" (2 thread cung doc gia tri ton kho cu, cung ghi de
 * len nhau).
 */
public class OrderBatchProcessor {

    private final OrderService orderService;

    public OrderBatchProcessor(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Xu ly (checkout) danh sach don hang SONG SONG bang mot thread pool co
     * kich thuoc co dinh, cho toi khi tat ca hoan tat roi tra ve ket qua.
     */
    public List<Order> processBatch(List<Order> orders, PaymentMethod paymentMethod, int threadPoolSize) {
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        List<Order> processedOrders = new ArrayList<>();
        try {
            List<Callable<Order>> tasks = orders.stream()
                    .<Callable<Order>>map(order -> () -> orderService.checkout(order, paymentMethod))
                    .toList();

            // invokeAll: gui tat ca task vao pool, block cho den khi TAT CA hoan tat.
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
