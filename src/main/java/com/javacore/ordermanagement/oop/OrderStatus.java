package com.javacore.ordermanagement.oop;

/**
 * ENUM: tap gia tri huu han va an toan kieu (type-safe) cho trang thai don
 * hang. Dung enum thay vi String("PENDING", "pending", "Pending"...) giup
 * trinh bien dich bat loi go sai chinh ta va cho phep dung switch de xu ly
 * day du tung truong hop.
 */
public enum OrderStatus {
    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
