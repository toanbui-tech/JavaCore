package com.javacore.ordermanagement.exception;

/**
 * UNCHECKED EXCEPTION (extends RuntimeException).
 * <p>
 * "Khong tim thay san pham voi id da cho" thuong la dau hieu cua mot LOI LAP
 * TRINH (goi sai id, du lieu khong dong bo) chu khong phai tinh huong nghiep
 * vu binh thuong nguoi goi can xu ly moi lan. Vi vay dung unchecked de khong
 * bat ep moi noi goi phai viet try/catch thua thai.
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String productId) {
        super("Khong tim thay san pham voi id='%s'".formatted(productId));
    }
}
