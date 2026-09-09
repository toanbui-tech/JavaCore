package com.javacore.ordermanagement.oop;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * OrderItem là một "value object" nhỏ: gắn một Product với số lượng được mua
 * trong một đơn hàng cụ thể. Tách riêng khỏi Product để giá tại thời điểm mua
 * (không bị ảnh hưởng nếu sau này Product đổi giá) có thể được lưu lại.
 */
public class OrderItem {

    private final Product product;
    private final int quantity;
    private final BigDecimal priceAtPurchase;

    public OrderItem(Product product, int quantity) {
        this.product = Objects.requireNonNull(product);
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity phai > 0");
        }
        this.quantity = quantity;
        this.priceAtPurchase = product.getPrice();
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getSubtotal() {
        return priceAtPurchase.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public String toString() {
        return "%dx %s = %s".formatted(quantity, product.getName(), getSubtotal());
    }
}
