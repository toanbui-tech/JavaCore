package com.javacore.ordermanagement.io;

import com.javacore.ordermanagement.oop.Product;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * XỬ LÝ I/O CƠ BẢN - xuất JSON KHÔNG dùng thư viện ngoài (Gson/Jackson) để
 * giữ đúng tinh thần "Java Core thuần": tự ghép chuỗi JSON hợp lệ bằng
 * StringBuilder/Stream, rồi ghi ra file bằng java.nio.file.Files.
 * <p>
 * Trong dự án thực tế (Java FullStack sau này) bạn sẽ dùng Jackson/Gson thay
 * vì tự viết như thế này - ở đây mục đích là HIỂU BẢN CHẤT của serialization,
 * không phải khuyến khích tự viết JSON writer cho production.
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
