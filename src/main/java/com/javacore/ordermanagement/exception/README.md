# package `exception`

## Khái niệm

- **Checked exception**: `OutOfStockException` (extends `Exception`) — trình
  biên dịch bắt buộc nơi gọi `Product.reduceStock()` phải `try/catch` hoặc
  khai báo `throws`. Dùng cho lỗi nghiệp vụ *có thể dự đoán trước và người
  gọi bắt buộc phải xử lý* (hết hàng khi đặt mua).
- **Unchecked exception**: `ProductNotFoundException`,
  `CustomerNotFoundException`, `InvalidOrderException` (extends
  `RuntimeException`) — không bắt buộc `try/catch`. Dùng cho lỗi thường là
  *bug/lập trình sai* hoặc vi phạm quy tắc nghiệp vụ mà không cần bắt ở mọi
  nơi gọi (id không tồn tại, đơn hàng rỗng).

## Khi nào dùng checked vs unchecked

Quy tắc thực dụng: tự hỏi **"nếu lỗi này xảy ra, người gọi có thể làm gì đó
KHÁC ngay tại chỗ gọi để xử lý hợp lý không?"**

- **Có** → dùng **checked**. Đây là tín hiệu "tình huống này CÓ THỂ xảy ra
  trong vận hành bình thường, bạn phải nghĩ đến nó" (ví dụ: hết hàng → hỏi
  khách đổi số lượng, gợi ý sản phẩm thay thế, hủy đơn...).
- **Không** (chỉ có thể log lại, trả lỗi chung, hoặc đây thực chất là **bug**)
  → dùng **unchecked**, để tránh ép mọi lớp trung gian phải khai báo `throws`
  không cần thiết.

### Bảng ánh xạ trong project này

| Exception | Loại | Vì sao |
|---|---|---|
| `OutOfStockException` | Checked | Hết hàng là tình huống nghiệp vụ thường xuyên xảy ra, người gọi (`OrderService.placeOrder`) có thể phản ứng khác nhau. |
| `ProductNotFoundException`, `CustomerNotFoundException` | Unchecked | id không tồn tại thường là lỗi lập trình/dữ liệu không đồng bộ, không phải thứ mọi nơi gọi cần `try/catch` riêng. |
| `InvalidOrderException` | Unchecked | Vi phạm quy tắc nghiệp vụ (đơn rỗng, đổi trạng thái đơn đã đóng) — không có cách "sửa và thử lại ngay" trong luồng xử lý bình thường. |
| `IOException` (dùng trong `io/CsvExporter`) | Checked (của JDK) | Lỗi hệ thống (đĩa đầy, mất quyền ghi) ngoài tầm kiểm soát logic, nhưng JDK vẫn chọn checked vì I/O là nơi lỗi cực kỳ phổ biến và người gọi thường cần quyết định (retry, báo lỗi, ghi log khác...). |

### Cạm bẫy thường gặp

1. **Lạm dụng checked cho mọi thứ** → buộc code ở khắp nơi phải `try/catch`
   hoặc `throws` tràn lan, kể cả những lỗi không ai xử lý được gì ngoài log
   lại → code rối, dễ dẫn đến `catch (Exception e) {}` nuốt lỗi cho xong
   (rất tệ).
2. **Dùng unchecked cho lỗi nghiệp vụ quan trọng mà người gọi cần biết** →
   dễ bị bỏ sót vì compiler không nhắc, lỗi "rơi" ra runtime mới phát hiện.

### Xu hướng thực tế trong ngành

Java hiện đại (kể cả các framework như Spring) có xu hướng **ưu tiên
unchecked exception** — ngay cả checked exception của chính JDK
(`IOException`, `SQLException`) thường được wrap lại thành unchecked ở tầng
cao (`UncheckedIOException`, `DataAccessException` của Spring). Lý do:
checked exception phá vỡ khả năng dùng lambda/Stream một cách tự nhiên (thử
ném `OutOfStockException` bên trong một `.map(...)` sẽ không biên dịch được,
vì `Function` interface không khai báo `throws`). Vì vậy `OutOfStockException`
trong project này là một lựa chọn **có chủ đích, có chọn lọc** — không phải
khuôn mẫu để áp dụng tràn lan.
