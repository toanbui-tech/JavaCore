# package `stream`

## Khái niệm

- **Stream API & lambda**: `ReportService` dùng `filter/map/reduce/collect`
  với lambda expression để tính toán báo cáo (tổng doanh thu, top sản phẩm
  bán chạy, doanh thu theo khách hàng...) mà không cần vòng lặp `for` và biến
  tạm thủ công.
- **Method reference** (`Order::getTotal`, `OrderItem::getProduct`) là dạng
  rút gọn của lambda khi lambda chỉ gọi lại một method có sẵn.
- **Collectors.groupingBy/reducing/summingLong** — các "công thức" tổng hợp
  dữ liệu dựng sẵn, thường dùng hơn tự viết vòng lặp gom nhóm thủ công.

## Khi nào dùng

Dùng Stream khi bài toán là "biến đổi/tổng hợp một tập dữ liệu" (lọc, gom
nhóm, tính tổng, sắp xếp, lấy top-N...). Ưu tiên đọc code theo hướng khai báo
(declarative — mô tả *kết quả mong muốn*) thay vì mệnh lệnh (imperative — mô
tả *từng bước thực hiện*). Tránh dùng Stream khi logic có nhiều side-effect
phức tạp hoặc cần debug từng bước — lúc đó vòng lặp `for` truyền thống có thể
rõ ràng hơn.
