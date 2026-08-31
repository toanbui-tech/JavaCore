package com.javacore.ordermanagement.oop;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * OrderItem la mot "value object" nho: gan mot Product voi so luong duoc mua
 * trong mot don hang cu the. Tach rieng khoi Product de gia tai thoi diem mua
 * (khong bi anh huong neu sau nay Product doi gia) co the duoc luu lai.
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
