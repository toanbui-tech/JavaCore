package com.javacore.ordermanagement.io;

import com.javacore.ordermanagement.oop.Product;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * XU LY I/O CO BAN - xuat JSON KHONG dung thu vien ngoai (Gson/Jackson) de
 * giu dung tinh than "Java Core thuan": tu ghep chuoi JSON hop le bang
 * StringBuilder/Stream, roi ghi ra file bang java.nio.file.Files.
 * <p>
 * Trong du an thuc te (Java FullStack sau nay) ban se dung Jackson/Gson thay
 * vi tu viet nhu the nay - o day muc dich la HIEU BAN CHAT cua serialization,
 * khong phai khuyen khich tu viet JSON writer cho production.
 */
public class JsonExporter {

    public void exportProducts(List<Product> products, Path targetFile) throws IOException {
        String json = toJsonArray(products);
        Files.writeString(targetFile, json, StandardCharsets.UTF_8);
    }

    private String toJsonArray(List<Product> products) {
        String items = products.stream()
                .map(this::toJsonObject)
                .collect(Collectors.joining(",\n  "));
        return "[\n  %s\n]".formatted(items);
    }

    private String toJsonObject(Product p) {
        return """
                {"id": "%s", "name": "%s", "category": "%s", "price": %s, "stock": %d}"""
                .formatted(escape(p.getId()), escape(p.getName()), escape(p.getCategory()), p.getPrice(), p.getStockQuantity());
    }

    private String escape(String value) {
        return value.replace("\"", "\\\"");
    }
}
