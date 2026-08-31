package com.javacore.ordermanagement.io;

import com.javacore.ordermanagement.oop.Order;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * XU LY I/O CO BAN - ghi file.
 * <p>
 * try-with-resources dam bao BufferedWriter luon duoc dong (close()) du co
 * xay ra ngoai le hay khong - tuong duong voi try/finally thu cong nhung
 * ngan gon va it loi hon (khong the quen goi close()).
 * <p>
 * IOException la mot CHECKED EXCEPTION cua chinh JDK (khac voi cac custom
 * exception trong package exception): loi doc/ghi file la loi he thong
 * (disk day, khong co quyen...) hoan toan nam ngoai tam kiem soat cua logic
 * nghiep vu, nen Java bat nguoi goi phai chu dong xu ly hoac khai bao throws.
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
