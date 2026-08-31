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

Quy tắc thực dụng: nếu người gọi **hoàn toàn có thể và nên** viết logic xử lý
khác nhau ngay tại chỗ gọi (ví dụ: hết hàng → hỏi khách có muốn đặt sản phẩm
khác không) thì dùng checked. Nếu lỗi thường chỉ có thể xử lý ở tầng rất cao
(log lại, trả lỗi 4xx cho client) thì dùng unchecked để tránh ép mọi lớp
trung gian phải khai báo `throws` không cần thiết. Java hiện đại (kể cả các
framework như Spring) có xu hướng ưu tiên unchecked exception, checked
exception chỉ nên dùng có chọn lọc như ví dụ `OutOfStockException` ở đây.
