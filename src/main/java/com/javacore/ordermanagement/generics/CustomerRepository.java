package com.javacore.ordermanagement.generics;

import com.javacore.ordermanagement.oop.Customer;

public class CustomerRepository extends AbstractInMemoryRepository<Customer, String> {

    public CustomerRepository() {
        super(Customer::getId);
    }
}
