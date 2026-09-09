package com.javacore.ordermanagement.oop;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * ABSTRACT CLASS + ENCAPSULATION.
 * <p>
 * Customer là "khuôn" chung cho mọi loại khách hàng. Nó không thể bị khởi tạo
 * trực tiếp (new Customer(...) là lỗi biên dịch) vì một khách hàng trong thực
 * tế luôn phải thuộc một hạng (Regular/VIP...) cụ thể.
 * <p>
 * Các field được khai báo private và chỉ lộ ra ngoài qua getter -> đây là
 * ENCAPSULATION: ẩn chi tiết cài đặt, chỉ cho phép truy cập/thay đổi trạng
 * thái thông qua các phương thức được kiểm soát.
 */
public abstract class Customer {

    private final String id;
    private final String name;
    private final String email;

    protected Customer(String id, String name, String email) {
        this.id = Objects.requireNonNull(id, "id khong duoc null");
        this.name = Objects.requireNonNull(name, "name khong duoc null");
        this.email = Objects.requireNonNull(email, "email khong duoc null");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    /**
     * ABSTRACTION + POLYMORPHISM (runtime binding).
     * <p>
     * Mỗi loại khách hàng tính mức giảm giá khác nhau. OrderService chỉ gọi
     * customer.calculateDiscount(...) mà không cần biết cụ thể là
     * RegularCustomer hay VipCustomer - JVM sẽ tự chọn phiên bản phù hợp lúc
     * chạy chương trình (dynamic dispatch).
     * Hãy dùng Abstract Class khi
     * các lớp con của bạn có chung một nguồn gốc bản chất 
     * và bạn muốn tái sử dụng code để tránh lặp lại chính mình (DRY - Don't Repeat Yourself).
     */
    public abstract BigDecimal calculateDiscount(BigDecimal orderTotal);

    /** Tên hạng khách hàng, dùng để hiển thị/báo cáo. */
    public abstract String getMembershipTier();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer other)) return false; // pattern matching for instanceof (Java 16+)
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "%s{id='%s', name='%s', tier=%s}".formatted(getClass().getSimpleName(), id, name, getMembershipTier());
    }
}
