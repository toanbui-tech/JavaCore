package com.javacore.ordermanagement.oop;

import java.math.BigDecimal;

/**
 * KẾ THỪA (inheritance): WholesaleCustomer "là một" Customer, nhận lại
 * id/name/email qua CONSTRUCTOR CHAIN bằng super(id, name, email) - không cần
 * viết lại phần lưu trữ/validate dữ liệu chung, chỉ tập trung vào phần RIÊNG
 * của khách hàng sỉ (companyName, taxId, minOrderValue).
 * <p>
 * ĐA HÌNH (polymorphism): giống RegularCustomer/VipCustomer, lớp này OVERRIDE
 * calculateDiscount() và getMembershipTier(). Khi OrderService/Order gọi
 * customer.calculateDiscount(...) qua tham chiếu kiểu Customer, JVM tự động
 * chọn đúng phiên bản của WholesaleCustomer lúc runtime (dynamic dispatch) -
 * code gọi hoàn toàn không biết (và không cần biết) đang làm việc với loại
 * khách hàng cụ thể nào.
 * <p>
 * QUAN TRỌNG - HỢP ĐỒNG (contract) của abstract method: Customer khai báo
 * calculateDiscount() phải trả về SỐ TIỀN được giảm (không phải TỶ LỆ), vì
 * Order.getTotal() làm subtotal.subtract(discount) trực tiếp. Mỗi lớp con
 * (Regular/VIP/Wholesale) BẮT BUỘC phải tuân thủ đúng hợp đồng này - đây
 * chính là nguyên tắc Liskov Substitution: Order phải dùng được với BẤT KỲ
 * lớp con nào mà không cần biết chi tiết, nếu một lớp con trả sai kiểu giá
 * trị thì đa hình sẽ "chạy đúng cú pháp nhưng sai nghĩa" (biên dịch qua
 * nhưng kết quả nghiệp vụ sai).
 * <p>
 * ENCAPSULATION: companyName/taxId/minOrderValue là private final, chỉ lộ ra
 * ngoài qua getter - tầng service (OrderService) đọc minOrderValue qua
 * getMinOrderValue() để kiểm tra điều kiện đơn hàng tối thiểu, không được
 * phép tự tiện thay đổi giá trị này.
 */
public class WholesaleCustomer extends Customer {

    private final String companyName;
    private final String taxId;
    private final BigDecimal minOrderValue;

    // Ngưỡng chiết khấu theo bậc (tier) - càng mua nhiều, tỷ lệ giảm càng cao.
    private static final BigDecimal TIER_3 = BigDecimal.valueOf(100_000_000);
    private static final BigDecimal TIER_2 = BigDecimal.valueOf(50_000_000);
    private static final BigDecimal TIER_1 = BigDecimal.valueOf(10_000_000);

    private static final BigDecimal RATE_TIER_3 = new BigDecimal("0.15"); // 15%
    private static final BigDecimal RATE_TIER_2 = new BigDecimal("0.10"); // 10%
    private static final BigDecimal RATE_TIER_1 = new BigDecimal("0.05"); // 5%
    private static final BigDecimal RATE_TIER_0 = BigDecimal.ZERO;

    public WholesaleCustomer(String id, String name, String email, String companyName, String taxId, BigDecimal minOrderValue) {
        super(id, name, email);
        this.companyName = companyName;
        this.taxId = taxId;
        this.minOrderValue = minOrderValue;
    }

    /**
     * Chiết khấu theo bậc: trả về SỐ TIỀN được giảm = orderTotal * tỷ lệ của
     * bậc tương ứng (không trả thẳng tỷ lệ, xem giải thích hợp đồng ở javadoc
     * lớp).
     */
    @Override
    public BigDecimal calculateDiscount(BigDecimal orderTotal) {
        BigDecimal rate = resolveDiscountRate(orderTotal);
        return orderTotal.multiply(rate);
    }

    private BigDecimal resolveDiscountRate(BigDecimal orderTotal) {
        if (orderTotal.compareTo(TIER_3) >= 0) {
            return RATE_TIER_3;
        } else if (orderTotal.compareTo(TIER_2) >= 0) {
            return RATE_TIER_2;
        } else if (orderTotal.compareTo(TIER_1) >= 0) {
            return RATE_TIER_1;
        } else {
            return RATE_TIER_0;
        }
    }

    @Override
    public String getMembershipTier() {
        return "WHOLESALE";
    }

    public BigDecimal getMinOrderValue() {
        return minOrderValue;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getTaxId() {
        return taxId;
    }
}
