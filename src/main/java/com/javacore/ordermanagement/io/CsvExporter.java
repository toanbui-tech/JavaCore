package com.javacore.ordermanagement.io;

import com.javacore.ordermanagement.oop.Order;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * XỬ LÝ I/O CƠ BẢN - ghi file.
 * <p>
 * try-with-resources đảm bảo BufferedWriter luôn được đóng (close()) dù có
 * xảy ra ngoại lệ hay không - tương đương với try/finally thủ công nhưng
 * ngắn gọn và ít lỗi hơn (không thể quên gọi close()).
 * <p>
 * IOException là một CHECKED EXCEPTION của chính JDK (khác với các custom
 * exception trong package exception): lỗi đọc/ghi file là lỗi hệ thống
 * (đĩa đầy, không có quyền...) hoàn toàn nằm ngoài tầm kiểm soát của logic
 * nghiệp vụ, nên Java bắt người gọi phải chủ động xử lý hoặc khai báo throws.
 */
public class CsvExporter {

    private static final String HEADER = "order_id,customer_name,status,total";

    public void exportOrders(List<Order> orders, Path targetFile) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(targetFile, StandardCharsets.UTF_8)) {
            writer.write(HEADER);
            writer.newLine();
            for (Order order : orders) {
                writer.write(toCsvLine(order));
                writer.newLine();
            }
        }
    }

    private String toCsvLine(Order order) {
        return "%s,%s,%s,%s".formatted(
                order.getId(),
                order.getCustomer().getName().replace(",", " "),
                order.getStatus(),
                order.getTotal());
    }
}
