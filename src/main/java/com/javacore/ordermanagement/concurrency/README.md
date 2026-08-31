# package `concurrency`

## Khái niệm

- **Thread & Runnable**: `NotificationSender` tạo một `Thread` mới cho mỗi
  thông báo cần gửi, để không chặn (block) luồng chính. Đơn giản nhưng tốn
  kém nếu số lượng tác vụ lớn (mỗi Thread tốn bộ nhớ + chi phí tạo/hủy).
- **ExecutorService, Callable, Future**: `OrderBatchProcessor` dùng một
  thread pool cố định (`Executors.newFixedThreadPool`) để xử lý (checkout)
  nhiều đơn hàng song song, giới hạn số luồng chạy cùng lúc. `Callable<Order>`
  (khác `Runnable`) cho phép trả về kết quả và ném checked exception;
  `Future<Order>` là "phiếu nhận" để lấy kết quả sau khi tác vụ hoàn tất.
- **Thread-safety**: `Product.reduceStock()`/`restock()` được đánh dấu
  `synchronized` vì nhiều luồng có thể cùng sửa tồn kho của cùng một sản
  phẩm — nếu không đồng bộ hóa sẽ xảy ra race condition (lost update).

## Khi nào dùng

- Dùng `Thread`/`Runnable` trực tiếp chỉ cho tác vụ đơn lẻ, không thường
  xuyên.
- Dùng `ExecutorService` khi cần chạy **nhiều** tác vụ tương tự nhau, muốn
  kiểm soát số luồng song song, và cần lấy lại kết quả/exception của từng
  tác vụ.
- Luôn đặt câu hỏi "dữ liệu nào đang được nhiều luồng cùng đọc/ghi?" — nếu
  có, cần đồng bộ hóa (`synchronized`, `Atomic*`, hoặc cấu trúc dữ liệu
  concurrent-safe) để tránh lỗi khó tái hiện (chỉ xảy ra ngẫu nhiên khi chạy
  thật, rất khó debug).
