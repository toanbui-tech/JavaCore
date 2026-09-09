package com.javacore.ordermanagement.oop.payment;

import java.math.BigDecimal;

/**
 * ĐA HÌNH QUA INTERFACE: implements PaymentMethod giống CashPayment,
 * CreditCardPayment, EWalletPayment — OrderService chỉ gọi pay(amount) mà
 * không cần biết đây là chuyển khoản ngân hàng.
 * <p>
 * ENCAPSULATION: các field (ngân hàng, mã giao dịch, số tiền đã xác nhận)
 * là private final — không ai có thể sửa "số tiền đã nhận" sau khi đối
 * tượng được tạo ra, tránh giả mạo kết quả đối soát.
 * <p>
 * KHÁC BIỆT NGHIỆP VỤ so với các PaymentMethod còn lại: chuyển khoản ngân
 * hàng KHÔNG có xác nhận tức thời như thẻ/ví — trong thực tế phải chờ tiền
 * về rồi đối soát (reconciliation) với sao kê ngân hàng bằng cách so khớp
 * số tiền và nội dung chuyển khoản (transferReferenceCode). Ở đây ta giả
 * lập bước đối soát đó đã hoàn tất từ trước (receivedAmount được truyền
 * sẵn vào constructor), pay() chỉ làm nhiệm vụ SO KHỚP số tiền đã xác nhận
 * với số tiền cần thanh toán, khác với CreditCardPayment (kiểm tra hạn
 * mức) hay EWalletPayment (trừ số dư).
 */
public class BankTransferPayment implements PaymentMethod {

    private final String bankName;
    private final String transferReferenceCode;
    private final BigDecimal receivedAmount;

    public BankTransferPayment(String bankName, String transferReferenceCode, BigDecimal receivedAmount) {
        this.bankName = bankName;
        this.transferReferenceCode = transferReferenceCode;
        this.receivedAmount = receivedAmount;
    }

    @Override
    public boolean pay(BigDecimal amount) {
        if (receivedAmount.compareTo(amount) < 0) {
            System.out.printf("[BANK_TRANSFER] Chưa nhận đủ tiền từ %s (mã GD: %s): cần %s, đã nhận %s.%n",
                    bankName, transferReferenceCode, amount, receivedAmount);
            return false;
        }
        System.out.printf("[BANK_TRANSFER] Đối soát thành công giao dịch từ %s (mã GD: %s): %s.%n",
                bankName, transferReferenceCode, amount);
        return true;
    }

    @Override
    public String getMethodName() {
        return "BANK_TRANSFER";
    }
}
