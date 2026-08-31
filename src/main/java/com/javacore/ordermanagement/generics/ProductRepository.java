package com.javacore.ordermanagement.generics;

import com.javacore.ordermanagement.oop.Product;

import java.util.List;

public class ProductRepository extends AbstractInMemoryRepository<Product, String> {

    public ProductRepository() {
        super(Product::getId); // method reference: cach AbstractInMemoryRepository lay id tu mot Product
    }

    /** Vi du finder tuy bien them ngoai CRUD co ban, dung Stream API. */
    public List<Product> findByCategory(String category) {
        return findAll().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .toList();
    }
}
