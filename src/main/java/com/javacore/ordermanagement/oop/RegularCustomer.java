package com.javacore.ordermanagement.oop;

import java.math.BigDecimal;

/**
 * KẾ THỪA (inheritance): RegularCustomer "là một" Customer, thừa hưởng
 * id/name/email và chỉ phải hiện thực phần riêng của nó (calculateDiscount).
 */
public class RegularCustomer extends Customer {

    private static final BigDecimal DISCOUNT_RATE = BigDecimal.ZERO;

    public RegularCustomer(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal orderTotal) {
        // Khách hàng thường: không giảm giá.
        return orderTotal.multiply(DISCOUNT_RATE);
    }

    @Override
    public String getMembershipTier() {
        return "REGULAR";
    }
}
