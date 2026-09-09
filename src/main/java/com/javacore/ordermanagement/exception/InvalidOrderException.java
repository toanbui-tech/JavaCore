package com.javacore.ordermanagement.exception;

/**
 * UNCHECKED EXCEPTION - đại diện cho vi phạm QUY TẮC NGHIỆP VỤ của đơn hàng:
 * đơn rỗng (không có item nào), hoặc cố gắng đổi trạng thái của một đơn đã
 * ở trạng thái cuối (DELIVERED/CANCELLED). Đây là lỗi mà người gọi thường
 * KHÔNG thể "khắc phục và thử lại ngay" trong luồng xử lý bình thường, nên
 * để unchecked, thay vì bắt mọi nơi phải try/catch.
 */
public class InvalidOrderException extends RuntimeException {

    public InvalidOrderException(String message) {
        super(message);
    }
}
