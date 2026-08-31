# Order Management Core — Ôn tập Java Core

Dự án Maven thuần Java (không dùng Spring hay bất kỳ framework nào) để ôn lại
nền tảng Java Core trước khi học Java FullStack. Toàn bộ khái niệm được áp
dụng vào **một bối cảnh nghiệp vụ xuyên suốt: hệ thống Quản lý đơn hàng**
(Order Management) — sản phẩm, khách hàng, đặt hàng, thanh toán, xử lý hàng
loạt, xuất báo cáo — thay vì các bài tập rời rạc không liên quan nhau.

## Yêu cầu

- JDK 17+
- Maven 3.8+

## Chạy thử

```bash
mvn compile exec:java
```

Chương trình sẽ chạy toàn bộ luồng demo (tạo sản phẩm/khách hàng, đặt hàng,
xử lý hàng đợi ưu tiên, báo cáo, xử lý đa luồng, xuất file) và in log ra
console. File kết quả được ghi vào thư mục `output/` (`orders.csv`,
`products.json`).

## Chạy unit test

```bash
mvn test
```

## Cấu trúc package

```
com.javacore.ordermanagement
├── oop/            OOP: class, abstract class, interface, kế thừa, đa hình, encapsulation
│   └── payment/     interface PaymentMethod + các cách thanh toán cụ thể
├── exception/       Exception Handling: checked vs unchecked, custom exception
├── generics/        Generics: Repository<T, ID> tái sử dụng cho mọi entity
├── collections/     Collections Framework: chọn đúng Map/Set/Queue cho từng bài toán
├── stream/          Java Stream API & lambda: báo cáo/thống kê
├── io/               I/O cơ bản: xuất CSV/JSON
├── concurrency/     Đa luồng: Thread/Runnable, ExecutorService/Callable/Future
├── service/         Tầng service phối hợp mọi thứ lại thành nghiệp vụ hoàn chỉnh
└── Main.java        Điểm vào, chạy demo toàn bộ luồng
```

Mỗi package (trừ `Main`/`service`) có một `README.md` riêng nêu: khái niệm
là gì, dùng khi nào, và class nào trong package minh họa khái niệm đó.

## Lộ trình nên code/đọc theo thứ tự

Đừng đọc/code hết một lần — hãy đi theo lộ trình dưới đây, từ nền tảng đến
nâng cao, để hiểu sâu dần và có thể tự giải thích được từng quyết định thiết
kế khi phỏng vấn:

1. **`oop/`** — Bắt đầu ở đây. Đọc `Customer` (abstract class) rồi
   `RegularCustomer`/`VipCustomer` để thấy kế thừa + đa hình. Sau đó
   `Product`, `OrderItem`, `Order` để thấy encapsulation + composition. Thử
   tự viết thêm một loại `Customer` mới (ví dụ `WholesaleCustomer`) để chắc
   tay trước khi qua bước tiếp theo.
2. **`oop/payment/`** — So sánh interface (`PaymentMethod`) với abstract
   class (`Customer`) ở bước 1: khi nào chọn cái nào. Thử tự viết thêm một
   `PaymentMethod` mới (ví dụ `BankTransferPayment`).
3. **`exception/`** — Đọc 4 exception, phân biệt rõ `OutOfStockException`
   (checked) với 3 exception còn lại (unchecked). Xem `Product.reduceStock()`
   và `OrderService.placeOrder()` để thấy checked exception "ép" người gọi
   phải xử lý như thế nào.
4. **`generics/`** — Đọc `Repository<T, ID>` rồi `AbstractInMemoryRepository`
   để hiểu generics giúp tái sử dụng logic CRUD ra sao. Thử tự thêm một
   repository mới nếu bạn thêm entity mới ở bước 1.
5. **`collections/`** — So sánh `ProductCatalog` (Map + Set) với
   `OrderQueueManager` (Queue + PriorityQueue). Tự hỏi: "Nếu đổi PriorityQueue
   thành Queue thường thì hành vi thay đổi thế nào?" — rồi thử sửa code để
   kiểm chứng.
6. **`stream/`** — Đọc `ReportService`, đây là nơi OOP (bước 1-2) +
   Collections (bước 5) được "truy vấn" bằng Stream API/lambda. Thử tự thêm
   một báo cáo mới, ví dụ "doanh thu theo category".
7. **`io/`** — Đọc `CsvExporter`/`JsonExporter`, chú ý try-with-resources và
   sự khác biệt giữa `IOException` (checked, của JDK) với custom exception ở
   bước 3.
8. **`concurrency/`** — Khó nhất, nên để cuối. Đọc `NotificationSender`
   (Thread/Runnable thô) trước, rồi `OrderBatchProcessor`
   (ExecutorService/Callable/Future) để thấy vì sao thread pool tốt hơn tạo
   Thread thủ công. Chú ý phần `synchronized` trong `Product` — đây là ví dụ
   thực tế của race condition và cách phòng tránh.
9. **`service/OrderService.java` + `src/test/`** — Cuối cùng, đọc lại
   `OrderService` như một bức tranh tổng thể (nó dùng tất cả các package
   trên), rồi đọc unit test tương ứng để thấy cách kiểm thử từng nhánh hành
   vi (thành công, hết hàng, không tìm thấy khách hàng, đơn rỗng...).

Sau khi đi hết lộ trình, thử tự thêm một tính năng mới xuyên suốt nhiều
package (ví dụ: "áp mã giảm giá" hoặc "giới hạn số đơn VIP xử lý đồng thời")
để luyện khả năng phối hợp nhiều khái niệm cùng lúc — đúng như khi làm việc
thực tế.
