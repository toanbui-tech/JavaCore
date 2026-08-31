package com.javacore.ordermanagement.oop;

import com.javacore.ordermanagement.exception.OutOfStockException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * ENCAPSULATION: soLuongTon (stockQuantity) khong co public setter. Cach duy
 * nhat de thay doi ton kho tu ben ngoai la qua reduceStock()/restock(), noi
 * ta co the kiem soat rang buoc nghiep vu (khong duoc am, phai bao loi khi
 * khong du hang...). Neu de field public hoac setter tu do, code ben ngoai
 * co the gan gia tri am -> pha vo tinh toan ven du lieu.
 */
public class Product {

    private final String id;
    private final String name;
    private final String category;
    private final BigDecimal price;
    private int stockQuantity;

    public Product(String id, String name, String category, BigDecimal price, int stockQuantity) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.category = Objects.requireNonNull(category);
        this.price = Objects.requireNonNull(price);
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("stockQuantity khong duoc am");
        }
        this.stockQuantity = stockQuantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public synchronized int getStockQuantity() {
        // synchronized: nhieu luong (xem package concurrency) co the ban hang
        // cung luc, doc/ghi ton kho phai duoc dong bo hoa de tranh sai lech du lieu.
        return stockQuantity;
    }

    /**
     * CHECKED EXCEPTION o tang domain: khong du hang la mot tinh huong nghiep
     * vu HOAN TOAN CO THE XAY RA va nguoi goi BAT BUOC phai xu ly (bat hoac
     * khai bao throws) - vi vay OutOfStockException extends Exception chu
     * khong phai RuntimeException.
     */
    public synchronized void reduceStock(int quantity) throws OutOfStockException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity phai > 0");
        }
        if (quantity > stockQuantity) {
            throw new OutOfStockException(id, quantity, stockQuantity);
        }
        stockQuantity -= quantity;
    }

    public synchronized void restock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity phai > 0");
        }
        stockQuantity += quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{id='%s', name='%s', price=%s, stock=%d}".formatted(id, name, price, stockQuantity);
    }
}
