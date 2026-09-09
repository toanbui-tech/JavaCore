package com.javacore.ordermanagement.oop.payment;

import java.math.BigDecimal;

/** Thanh toán tiền mặt: luôn thành công ngay lập tức (đơn giản hóa cho mục đích học tập). */
public class CashPayment implements PaymentMethod {

    @Override
    public boolean pay(BigDecimal amount) {
        System.out.printf("[CASH] Da nhan %s tien mat.%n", amount);
        return true;
    }

    @Override
    public String getMethodName() {
        return "CASH";
    }
}
