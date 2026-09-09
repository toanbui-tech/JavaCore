package com.javacore.ordermanagement.exception;

/** UNCHECKED EXCEPTION - tương tự ProductNotFoundException nhưng cho Customer. */
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String customerId) {
        super("Khong tim thay khach hang voi id='%s'".formatted(customerId));
    }
}
