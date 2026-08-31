package com.javacore.ordermanagement.exception;

/**
 * UNCHECKED EXCEPTION - dai dien cho vi pham QUY TAC NGHIEP VU cua don hang:
 * don rong (khong co item nao), hoac co gang doi trang thai cua mot don da
 * o trang thai cuoi (DELIVERED/CANCELLED). Day la loi ma nguoi goi thuong
 * KHONG the "khac phuc va thu lai ngay" trong luong xu ly binh thuong, nen
 * de unchecked, thay vi bat moi noi phai try/catch.
 */
public class InvalidOrderException extends RuntimeException {

    public InvalidOrderException(String message) {
        super(message);
    }
}
