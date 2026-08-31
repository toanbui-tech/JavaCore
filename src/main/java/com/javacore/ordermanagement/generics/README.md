# package `generics`

## Khái niệm

- **Generics**: `Repository<T, ID>` và `AbstractInMemoryRepository<T, ID>`
  viết logic CRUD (save/findById/findAll/deleteById) MỘT LẦN, dùng lại được
  cho `Product`, `Customer`, `Order` (mỗi entity một `ID` kiểu `String`)
  nhưng vẫn được kiểm tra kiểu dữ liệu ở compile-time — không cần ép kiểu
  (cast) thủ công như khi dùng `Object`.

## Khi nào dùng

Dùng generics khi bạn thấy mình sắp copy-paste một class/method chỉ để đổi
kiểu dữ liệu xử lý (ví dụ viết `ProductRepository`, `CustomerRepository`,
`OrderRepository` với logic giống hệt nhau). Generics giúp viết một lần, tái
sử dụng an toàn, và là nền tảng bắt buộc phải hiểu trước khi học Spring Data
JPA (`JpaRepository<T, ID>` dùng chính xác pattern này).
