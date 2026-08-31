package com.javacore.ordermanagement.concurrency;

/**
 * DA LUONG CO BAN - Thread & Runnable (muc thap nhat).
 * <p>
 * Gui thong bao (email/sms gia lap) la mot tac vu I/O co do tre, khong nen
 * lam chan (block) luong chinh dang xu ly don hang. Cach don gian nhat la
 * tao 1 Thread moi cho moi thong bao va goi start().
 * <p>
 * Han che cua cach nay: moi lan goi la mot Thread MOI hoan toan (tao/huy
 * thread rat ton kem) - xem concurrency.OrderBatchProcessor de biet cach
 * lam tot hon voi ExecutorService khi can xu ly NHIEU tac vu.
 */
public class NotificationSender {

    public void sendAsync(String recipientEmail, String message) {
        Runnable task = () -> {
            try {
                Thread.sleep(200); // gia lap do tre goi API gui email/sms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // khoi phuc interrupt flag - thuc hanh chuan khi bat InterruptedException
                return;
            }
            System.out.printf("[%s] Da gui thong bao toi %s: %s%n",
                    Thread.currentThread().getName(), recipientEmail, message);
        };
        new Thread(task, "notification-thread").start();
    }
}
