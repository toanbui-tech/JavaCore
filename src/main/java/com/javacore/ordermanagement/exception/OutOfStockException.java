package com.javacore.ordermanagement.exception;

/**
 * CHECKED EXCEPTION (extends Exception, không phải RuntimeException).
 * <p>
 * Hết hàng là một lỗi NGHIỆP VỤ có thể dự đoán trước và người gọi nên bắt
 * buộc phải xử lý (ví dụ: gọi lại với số lượng khác, thông báo cho khách,
 * hủy đơn...). Trình biên dịch sẽ bắt lỗi nếu hàm gọi Product.reduceStock()
 * mà không bắt (try/catch) hoặc khai báo "throws OutOfStockException".
 */
public class OutOfStockException extends Exception {

    private final String productId;
    private final int requestedQuantity;
    private final int availableQuantity;

    public OutOfStockException(String productId, int requestedQuantity, int availableQuantity) {
        super("San pham '%s' khong du hang: yeu cau %d, con lai %d"
                .formatted(productId, requestedQuantity, availableQuantity));
        this.productId = productId;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }
}
