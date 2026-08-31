package com.javacore.ordermanagement.oop.payment;

import java.math.BigDecimal;

/** Thanh toan tien mat: luon thanh cong ngay lap tuc (don gian hoa cho muc dich hoc tap). */
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
