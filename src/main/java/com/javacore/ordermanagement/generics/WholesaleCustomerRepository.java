package com.javacore.ordermanagement.generics;

import com.javacore.ordermanagement.oop.WholesaleCustomer;

public class WholesaleCustomerRepository extends AbstractInMemoryRepository<WholesaleCustomer, String> {

    public WholesaleCustomerRepository() {
        super(WholesaleCustomer::getId);
    }
  
}
