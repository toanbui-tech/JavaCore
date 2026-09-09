package com.javacore.ordermanagement.oop;

import com.javacore.ordermanagement.exception.OutOfStockException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * ENCAPSULATION: số lượng tồn (stockQuantity) không có public setter. Cách
 * duy nhất để thay đổi tồn kho từ bên ngoài là qua reduceStock()/restock(),
 * nơi ta có thể kiểm soát ràng buộc nghiệp vụ (không được âm, phải báo lỗi
 * khi không đủ hàng...). Nếu để field public hoặc setter tự do, code bên
 * ngoài có thể gán giá trị âm -> phá vỡ tính toàn vẹn dữ liệu.
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
        // synchronized: nhiều luồng (xem package concurrency) có thể bán hàng
        // cùng lúc, đọc/ghi tồn kho phải được đồng bộ hóa để tránh sai lệch dữ liệu.
        return stockQuantity;
    }

    /**
     * CHECKED EXCEPTION ở tầng domain: không đủ hàng là một tình huống nghiệp
     * vụ HOÀN TOÀN CÓ THỂ XẢY RA và người gọi BẮT BUỘC phải xử lý (bắt hoặc
     * khai báo throws) - vì vậy OutOfStockException extends Exception chứ
     * không phải RuntimeException.
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
