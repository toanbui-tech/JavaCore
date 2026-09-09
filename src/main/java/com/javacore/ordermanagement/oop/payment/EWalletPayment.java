package com.javacore.ordermanagement.oop.payment;

import java.math.BigDecimal;

/** Thanh toán ví điện tử: giả lập số dư ví. */
public class EWalletPayment implements PaymentMethod {

    private BigDecimal balance;

    public EWalletPayment(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean pay(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            System.out.printf("[E_WALLET] So du khong du (%s < %s).%n", balance, amount);
            return false;
        }
        balance = balance.subtract(amount);
        System.out.printf("[E_WALLET] Da tru %s, con lai %s.%n", amount, balance);
        return true;
    }

    @Override
    public String getMethodName() {
        return "E_WALLET";
    }
}
