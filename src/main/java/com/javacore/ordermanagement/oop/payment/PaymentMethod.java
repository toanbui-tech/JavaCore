package com.javacore.ordermanagement.oop.payment;

import java.math.BigDecimal;

/**
 * INTERFACE: chi dinh nghia "hop dong" (contract) - bat ky hinh thuc thanh
 * toan nao cung phai co the pay(amount) va getMethodName(). Khac voi abstract
 * class Customer (co chung du lieu/hanh vi), interface o day chi mo ta HANH
 * VI, khong quan tam den cach hien thuc ben trong.
 * <p>
 * OrderService se cam vao 1 PaymentMethod bat ky (Cash/CreditCard/EWallet)
 * ma khong can biet chi tiet -> DA HINH THONG QUA INTERFACE.
 */
public interface PaymentMethod {

    /** @return true neu thanh toan thanh cong. */
    boolean pay(BigDecimal amount);

    String getMethodName();
}
