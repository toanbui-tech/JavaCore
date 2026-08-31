package com.javacore.ordermanagement.oop;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * KE THUA + OVERRIDE: VipCustomer cung "la mot" Customer nhung ghi de
 * (override) calculateDiscount() de tra ve mot ket qua khac hoan toan voi
 * RegularCustomer -> chinh la bieu hien ro nhat cua DA HINH (polymorphism).
 */
public class VipCustomer extends Customer {

    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.10"); // VIP giam 10%

    public VipCustomer(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal orderTotal) {
        return orderTotal.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getMembershipTier() {
        return "VIP";
    }
}
