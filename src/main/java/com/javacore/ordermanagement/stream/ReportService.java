package com.javacore.ordermanagement.stream;

import com.javacore.ordermanagement.oop.Order;
import com.javacore.ordermanagement.oop.OrderItem;
import com.javacore.ordermanagement.oop.OrderStatus;
import com.javacore.ordermanagement.oop.Product;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JAVA STREAM API & LAMBDA EXPRESSION.
 * <p>
 * Thay vì viết vòng for/if lồng dòng để tính toán/thống kê thủ công, Stream
 * API mô tả "CÁI GÌ cần làm" (filter, map, group, reduce...) thay vì "LÀM
 * THẾ NÀO" (khai báo biến tạm, tăng chỉ số...) - code ngắn gọn, khai báo
 * (declarative) và dễ đọc hơn.
 * <p>
 * Lưu ý: các phương thức ở đây KHÔNG thay đổi (immutable) danh sách đầu vào,
 * chỉ đọc và trả về kết quả mới - đúng tính chất "không tác dụng phụ" mà
 * Stream khuyến khích.
 */
public class ReportService {

    /** Tổng doanh thu = tổng Order#getTotal() của các đơn đã hoàn tất (không tính đơn hủy). */
    public BigDecimal totalRevenue(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Gom nhóm đơn hàng theo trạng thái - Map<OrderStatus, List<Order>>. */
    public Map<OrderStatus, List<Order>> groupOrdersByStatus(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getStatus));
    }

    /** Doanh thu theo từng khách hàng (tên khách -> tổng tiền đã chi, các đơn đã hủy không tính). */
    public Map<String, BigDecimal> revenueByCustomerName(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .collect(Collectors.groupingBy(
                        order -> order.getCustomer().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Order::getTotal, BigDecimal::add)));
    }

    /** Top N sản phẩm bán chạy nhất theo tổng SỐ LƯỢNG đã bán, sắp xếp giảm dần. */
    public List<Map.Entry<Product, Long>> topSellingProducts(List<Order> orders, int topN) {
        Map<Product, Long> soldQuantityByProduct = orders.stream()
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(OrderItem::getProduct, Collectors.summingLong(OrderItem::getQuantity)));

        return soldQuantityByProduct.entrySet().stream()
                .sorted(Map.Entry.<Product, Long>comparingByValue().reversed())
                .limit(topN)
                .toList();
    }

    /** Giá trị trung bình mỗi đơn hàng (không tính đơn hủy). */
    public BigDecimal averageOrderValue(List<Order> orders) {
        List<Order> validOrders = orders.stream()
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .toList();
        if (validOrders.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = totalRevenue(validOrders);
        return total.divide(BigDecimal.valueOf(validOrders.size()), 2, java.math.RoundingMode.HALF_UP);
    }

    /** Danh sách đơn hàng có giá trị lớn nhất, sắp xếp giảm dần - ví dụ dùng Comparator + lambda. */
    public List<Order> topOrdersByValue(List<Order> orders, int topN) {
        return orders.stream()
                .sorted(Comparator.comparing(Order::getTotal).reversed())
                .limit(topN)
                .toList();
    }
}
