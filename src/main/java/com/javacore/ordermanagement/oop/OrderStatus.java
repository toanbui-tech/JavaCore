package com.javacore.ordermanagement.oop;

/**
 * ENUM: tập giá trị hữu hạn và an toàn kiểu (type-safe) cho trạng thái đơn
 * hàng. Dùng enum thay vì String("PENDING", "pending", "Pending"...) giúp
 * trình biên dịch bắt lỗi gõ sai chính tả và cho phép dùng switch để xử lý
 * đầy đủ từng trường hợp.
 */
public enum OrderStatus {
    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
