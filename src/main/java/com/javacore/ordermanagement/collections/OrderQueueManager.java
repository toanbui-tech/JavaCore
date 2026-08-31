package com.javacore.ordermanagement.collections;

import com.javacore.ordermanagement.oop.Order;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * COLLECTIONS FRAMEWORK - Queue:
 * <p>
 * Bai toan thuc te: don hang can duoc xep hang cho xu ly (giong quay
 * thanh toan sieu thi), nhung khach VIP nen duoc uu tien xu ly truoc.
 * <ul>
 *   <li>{@code Queue<Order>} (LinkedList) : hang doi FIFO don gian - dung khi
 *       moi don duoc doi xu ngang nhau, ai den truoc xu ly truoc.</li>
 *   <li>{@code PriorityQueue<Order>} : hang doi CO UU TIEN, tu dong sap xep
 *       phan tu theo Order#compareTo (VIP truoc, cung hang thi FIFO). Dung
 *       khi thu tu xu ly phu thuoc vao mot tieu chi nghiep vu chu khong chi
 *       don thuan la thoi gian den.</li>
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

    /** Luon tra ve don co uu tien cao nhat (VIP truoc, sau do theo thoi gian tao). */
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
