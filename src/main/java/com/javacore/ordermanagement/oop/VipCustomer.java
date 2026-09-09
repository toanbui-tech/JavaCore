package com.javacore.ordermanagement.oop;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * KẾ THỪA + OVERRIDE: VipCustomer cũng "là một" Customer nhưng ghi đè
 * (override) calculateDiscount() để trả về một kết quả khác hoàn toàn với
 * RegularCustomer -> chính là biểu hiện rõ nhất của ĐA HÌNH (polymorphism).
 */
public class VipCustomer extends Customer {

    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.10"); // VIP giảm 10%

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
