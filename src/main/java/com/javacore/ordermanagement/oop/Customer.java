package com.javacore.ordermanagement.oop;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * ABSTRACT CLASS + ENCAPSULATION.
 * <p>
 * Customer la "khuon" chung cho moi loai khach hang. No khong the bi khoi tao
 * truc tiep (new Customer(...) la loi bien dich) vi mot khach hang trong thuc
 * te luon phai thuoc mot hang (Regular/VIP...) cu the.
 * <p>
 * Cac field duoc khai bao private va chi lo ra ngoai qua getter -> day la
 * ENCAPSULATION: an chi tiet cai dat, chi cho phep truy cap/thay doi trang
 * thai thong qua cac phuong thuc duoc kiem soat.
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
     * Moi loai khach hang tinh muc giam gia khac nhau. OrderService chi goi
     * customer.calculateDiscount(...) ma khong can biet cu the la
     * RegularCustomer hay VipCustomer - JVM se tu chon phien ban phu hop luc
     * chay chuong trinh (dynamic dispatch).
     * Hãy dùng Abstract Class khi 
     * các lớp con của bạn có chung một nguồn gốc bản chất 
     * và bạn muốn tái sử dụng code để tránh lặp lại chính mình (DRY - Don't Repeat Yourself).
     */
    public abstract BigDecimal calculateDiscount(BigDecimal orderTotal);

    /** Ten hang khach hang, dung de hien thi/bao cao. */
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
