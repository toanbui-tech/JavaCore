package com.javacore.ordermanagement.oop.payment;

import java.math.BigDecimal;

/**
 * INTERFACE: chỉ định nghĩa "hợp đồng" (contract) - bất kỳ hình thức thanh
 * toán nào cũng phải có thể pay(amount) và getMethodName(). Khác với abstract
 * class Customer (có chung dữ liệu/hành vi), interface ở đây chỉ mô tả HÀNH
 * VI, không quan tâm đến cách hiện thực bên trong.
 * <p>
 * OrderService sẽ cắm vào 1 PaymentMethod bất kỳ (Cash/CreditCard/EWallet)
 * mà không cần biết chi tiết -> ĐA HÌNH THÔNG QUA INTERFACE.
 * Hãy dùng Interface khi bạn muốn thiết kế một "bản hợp đồng" (Contract) quy định các hành vi,
 * bất kể các đối tượng thực thi nó có thuộc cùng một họ hàng hay không.
 */
public interface PaymentMethod {

    /** @return true nếu thanh toán thành công. */
    boolean pay(BigDecimal amount);

    String getMethodName();
}
