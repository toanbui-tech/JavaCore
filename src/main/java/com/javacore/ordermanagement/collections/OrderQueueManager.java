package com.javacore.ordermanagement.collections;

import com.javacore.ordermanagement.oop.Order;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * COLLECTIONS FRAMEWORK - Queue:
 * <p>
 * Bài toán thực tế: đơn hàng cần được xếp hàng chờ xử lý (giống quầy
 * thanh toán siêu thị), nhưng khách VIP nên được ưu tiên xử lý trước.
 * <ul>
 *   <li>{@code Queue<Order>} (LinkedList) : hàng đợi FIFO đơn giản - dùng khi
 *       mọi đơn được đối xử ngang nhau, ai đến trước xử lý trước.</li>
 *   <li>{@code PriorityQueue<Order>} : hàng đợi CÓ ƯU TIÊN, tự động sắp xếp
 *       phần tử theo Order#compareTo (VIP trước, cùng hạng thì FIFO). Dùng
 *       khi thứ tự xử lý phụ thuộc vào một tiêu chí nghiệp vụ chứ không chỉ
 *       đơn thuần là thời gian đến.</li>
 * </ul>
 */
public class OrderQueueManager {

    private final Queue<Order> fifoQueue = new LinkedList<>();
    private final PriorityQueue<Order> priorityQueue = new PriorityQueue<>();

    public void enqueueFifo(Order order) {
        fifoQueue.offer(order);
    }

    public Order pollFifo() {
        return fifoQueue.poll();
    }

    public void enqueueByPriority(Order order) {
        priorityQueue.offer(order);
    }

    /** Luôn trả về đơn có ưu tiên cao nhất (VIP trước, sau đó theo thời gian tạo). */
    public Order pollByPriority() {
        return priorityQueue.poll();
    }

    public int priorityQueueSize() {
        return priorityQueue.size();
    }

    public boolean isPriorityQueueEmpty() {
        return priorityQueue.isEmpty();
    }
}
