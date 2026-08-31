package com.javacore.ordermanagement.collections;

import com.javacore.ordermanagement.oop.Product;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * COLLECTIONS FRAMEWORK - chon dung loai collection cho tung nhu cau:
 * <ul>
 *   <li>{@code Map<String, Product>} : tra cuu san pham theo id voi do phuc
 *       tap O(1) trung binh - phu hop khi can lookup lien tuc theo khoa.</li>
 *   <li>{@code Set<String>} (TreeSet) : luu danh sach TEN DANH MUC (category)
 *       KHONG TRUNG LAP va TU DONG SAP XEP theo alphabet - Set la lua chon
 *       tu nhien khi thu tu chen khong quan trong nhung tinh duy nhat thi co.</li>
 * </ul>
 * Day la lop rieng, tach biet voi generics.ProductRepository, de nguoi doc
 * de dang so sanh: repository (generic, tai su dung cho moi entity) vs.
 * catalog (chuyen biet, minh hoa cach chon collection phu hop tung bai toan).
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

    /** Danh sach danh muc, khong trung lap, da sap xep - nho dung TreeSet. */
    public Set<String> getCategories() {
        return Collections.unmodifiableSet(categories);
    }

    public int size() {
        return productsById.size();
    }
}
