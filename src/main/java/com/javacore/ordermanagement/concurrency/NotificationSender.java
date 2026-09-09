package com.javacore.ordermanagement.concurrency;

/**
 * ĐA LUỒNG CƠ BẢN - Thread & Runnable (mức thấp nhất).
 * <p>
 * Gửi thông báo (email/sms giả lập) là một tác vụ I/O có độ trễ, không nên
 * làm chặn (block) luồng chính đang xử lý đơn hàng. Cách đơn giản nhất là
 * tạo 1 Thread mới cho mỗi thông báo và gọi start().
 * <p>
 * Hạn chế của cách này: mỗi lần gọi là một Thread MỚI hoàn toàn (tạo/hủy
 * thread rất tốn kém) - xem concurrency.OrderBatchProcessor để biết cách
 * làm tốt hơn với ExecutorService khi cần xử lý NHIỀU tác vụ.
 */
public class NotificationSender {

    public void sendAsync(String recipientEmail, String message) {
        Runnable task = () -> {
            try {
                Thread.sleep(200); // giả lập độ trễ gọi API gửi email/sms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // khôi phục interrupt flag - thực hành chuẩn khi bắt InterruptedException
                return;
            }
            System.out.printf("[%s] Da gui thong bao toi %s: %s%n",
                    Thread.currentThread().getName(), recipientEmail, message);
        };
        new Thread(task, "notification-thread").start();
    }
}
