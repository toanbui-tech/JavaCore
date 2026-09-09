package com.javacore.ordermanagement.collections;

import com.javacore.ordermanagement.oop.Product;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * COLLECTIONS FRAMEWORK - chọn đúng loại collection cho từng nhu cầu:
 * <ul>
 *   <li>{@code Map<String, Product>} : tra cứu sản phẩm theo id với độ phức
 *       tạp O(1) trung bình - phù hợp khi cần lookup liên tục theo khóa.</li>
 *   <li>{@code Set<String>} (TreeSet) : lưu danh sách TÊN DANH MỤC (category)
 *       KHÔNG TRÙNG LẶP và TỰ ĐỘNG SẮP XẾP theo alphabet - Set là lựa chọn
 *       tự nhiên khi thứ tự chèn không quan trọng nhưng tính duy nhất thì có.</li>
 * </ul>
 * Đây là lớp riêng, tách biệt với generics.ProductRepository, để người đọc
 * dễ dàng so sánh: repository (generic, tái sử dụng cho mọi entity) vs.
 * catalog (chuyên biệt, minh họa cách chọn collection phù hợp từng bài toán).
 */
public class ProductCatalog {

    private final Map<String, Product> productsById = new HashMap<>();
    private final Set<String> categories = new TreeSet<>();

    public void add(Product product) {
        productsById.put(product.getId(), product);
        categories.add(product.getCategory());
    }

    public Product getById(String id) {
        return productsById.get(id);
    }

    /** Danh sách danh mục, không trùng lặp, đã sắp xếp - nhờ dùng TreeSet. */
    public Set<String> getCategories() {
        return Collections.unmodifiableSet(categories);
    }

    public int size() {
        return productsById.size();
    }
}
