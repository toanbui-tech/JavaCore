# package `oop.payment`

## Khái niệm

- **Interface**: `PaymentMethod` chỉ khai báo hành vi (`pay`, `getMethodName`),
  không có state hay logic chung.
- **Đa hình qua interface**: `OrderService.checkout()` nhận vào một
  `PaymentMethod` bất kỳ và chỉ gọi `pay(...)` — không cần biết đó là
  `CashPayment`, `CreditCardPayment` hay `EWalletPayment`.

## Khi nào dùng

Dùng interface khi bạn muốn nhiều lớp không liên quan về bản chất (tiền mặt,
thẻ, ví điện tử) vẫn có thể được xử lý đồng nhất bởi cùng một đoạn code, miễn
là chúng tuân theo cùng một "hợp đồng" hành vi. Đây cũng là nền tảng của
Dependency Injection sau này khi học Spring: code phụ thuộc vào interface,
không phụ thuộc vào implementation cụ thể.
