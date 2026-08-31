# package `oop`

## Khái niệm

- **Class & Encapsulation**: `Product`, `Order`, `OrderItem` — field `private`,
  chỉ thay đổi trạng thái qua method có kiểm soát (vd `Product.reduceStock()`
  không cho tồn kho âm).
- **Abstract class**: `Customer` — định nghĩa phần chung (id/name/email) và
  bắt buộc lớp con cài đặt `calculateDiscount()`.
- **Kế thừa (inheritance)**: `RegularCustomer`, `VipCustomer` kế thừa
  `Customer`.
- **Đa hình (polymorphism)**: `Order.getTotal()` gọi
  `customer.calculateDiscount(...)` mà không biết cụ thể là Regular hay VIP —
  hành vi thực tế được quyết định lúc chạy chương trình (runtime dispatch).
  Xem thêm `payment/` (đa hình qua interface).
- **Composition**: `Order` "có một" `Customer` và "có nhiều" `OrderItem`
  (khác với "is-a" của kế thừa).

## Khi nào dùng gì

- Dùng **abstract class** khi các lớp con chia sẻ chung dữ liệu/hành vi và có
  quan hệ "is-a" rõ ràng (VipCustomer *is a* Customer).
- Dùng **interface** (xem `payment/`) khi chỉ cần cam kết về hành vi, không
  quan tâm cách hiện thực, và các lớp cài đặt có thể không liên quan gì nhau
  về bản chất (Cash, CreditCard, EWallet không "là" nhau).
- Dùng **encapsulation** (field private + getter/setter có kiểm soát) ở mọi
  nơi lưu trạng thái quan trọng (tiền, tồn kho, trạng thái đơn hàng) để tránh
  dữ liệu bị đưa về trạng thái không hợp lệ từ bên ngoài.
