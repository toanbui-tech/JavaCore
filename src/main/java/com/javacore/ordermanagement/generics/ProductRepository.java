package com.javacore.ordermanagement.generics;

import com.javacore.ordermanagement.oop.Product;

import java.util.List;

public class ProductRepository extends AbstractInMemoryRepository<Product, String> {

    public ProductRepository() {
        super(Product::getId); // method reference: cách AbstractInMemoryRepository lấy id từ một Product
    }

    /** Ví dụ finder tùy biến thêm ngoài CRUD cơ bản, dùng Stream API. */
    public List<Product> findByCategory(String category) {
        return findAll().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .toList();
    }
}
