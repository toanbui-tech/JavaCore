package com.javacore.ordermanagement.exception;

/** UNCHECKED EXCEPTION - tuong tu ProductNotFoundException nhung cho Customer. */
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String customerId) {
        super("Khong tim thay khach hang voi id='%s'".formatted(customerId));
    }
}
