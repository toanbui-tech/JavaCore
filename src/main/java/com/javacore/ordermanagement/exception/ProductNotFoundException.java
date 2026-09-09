package com.javacore.ordermanagement.exception;

/**
 * UNCHECKED EXCEPTION (extends RuntimeException).
 * <p>
 * "Không tìm thấy sản phẩm với id đã cho" thường là dấu hiệu của một LỖI LẬP
 * TRÌNH (gọi sai id, dữ liệu không đồng bộ) chứ không phải tình huống nghiệp
 * vụ bình thường người gọi cần xử lý mỗi lần. Vì vậy dùng unchecked để không
 * bắt ép mọi nơi gọi phải viết try/catch thừa thãi.
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String productId) {
        super("Khong tim thay san pham voi id='%s'".formatted(productId));
    }
}
