package com.javacore.ordermanagement.oop;

import java.math.BigDecimal;

/**
 * KE THUA (inheritance): RegularCustomer "la mot" Customer, thua huong
 * id/name/email va chi phai hien thuc phan rieng cua no (calculateDiscount).
 */
public class RegularCustomer extends Customer {

    private static final BigDecimal DISCOUNT_RATE = BigDecimal.ZERO;

    public RegularCustomer(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal orderTotal) {
        // Khach hang thuong: khong giam gia.
        return orderTotal.multiply(DISCOUNT_RATE);
    }

    @Override
    public String getMembershipTier() {
        return "REGULAR";
    }
}
