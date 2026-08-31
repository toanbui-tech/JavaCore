# package `io`

## Khái niệm

- **Xử lý I/O cơ bản**: `CsvExporter` (ghi CSV bằng `BufferedWriter`) và
  `JsonExporter` (tự ghép chuỗi JSON, ghi bằng `Files.writeString`) minh họa
  cách đọc/ghi file bằng `java.nio.file` — API hiện đại hơn `java.io.File`
  cũ.
- **try-with-resources**: đảm bảo `BufferedWriter` luôn được đóng
  (`close()`) kể cả khi có exception xảy ra giữa chừng.
- **Checked exception của JDK**: `IOException` — khác với custom exception ở
  package `exception`, đây là lỗi hệ thống (đĩa đầy, không có quyền ghi...)
  nằm ngoài tầm kiểm soát của logic nghiệp vụ.

## Khi nào dùng

Xuất CSV/JSON thủ công như ở đây phù hợp cho mục đích **học và hiểu bản
chất** serialization. Trong dự án thực tế (đặc biệt khi học Java FullStack),
bạn sẽ dùng thư viện (Jackson/Gson cho JSON, Apache Commons CSV/OpenCSV cho
CSV) thay vì tự viết — nhưng hiểu cách nó hoạt động bên dưới giúp debug và
tùy biến tốt hơn khi thư viện không đáp ứng đủ nhu cầu.
