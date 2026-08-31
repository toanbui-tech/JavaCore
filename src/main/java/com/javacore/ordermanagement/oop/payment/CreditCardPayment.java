package com.javacore.ordermanagement.oop.payment;

import java.math.BigDecimal;

/** Thanh toan the tin dung: gia lap gioi han han muc (credit limit). */
public class CreditCardPayment implements PaymentMethod {

    private final String cardNumberMasked;
    private final BigDecimal creditLimit;

    public CreditCardPayment(String cardNumberMasked, BigDecimal creditLimit) {
        this.cardNumberMasked = cardNumberMasked;
        this.creditLimit = creditLimit;
    }

    @Override
    public boolean pay(BigDecimal amount) {
        if (amount.compareTo(creditLimit) > 0) {
            System.out.printf("[CREDIT_CARD] The %s vuot han muc (%s > %s).%n", cardNumberMasked, amount, creditLimit);
            return false;
        }
        System.out.printf("[CREDIT_CARD] Da tru %s tu the %s.%n", amount, cardNumberMasked);
        return true;
    }

    @Override
    public String getMethodName() {
        return "CREDIT_CARD";
    }
}
