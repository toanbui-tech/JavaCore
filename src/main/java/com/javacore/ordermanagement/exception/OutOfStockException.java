package com.javacore.ordermanagement.exception;

/**
 * CHECKED EXCEPTION (extends Exception, khong phai RuntimeException).
 * <p>
 * Het hang la mot loi NGHIEP VU co the du doan truoc va nguoi goi nen bat
 * buoc phai xu ly (vi du: goi lai voi so luong khac, thong bao cho khach,
 * huy don...). Trinh bien dich se bat loi neu ham goi Product.reduceStock()
 * ma khong bat (try/catch) hoac khai bao "throws OutOfStockException".
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
