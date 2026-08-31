# package `collections`

## Khái niệm

- **Map**: `ProductCatalog` dùng `HashMap<String, Product>` để tra cứu sản
  phẩm theo id với độ phức tạp trung bình O(1).
- **Set**: `ProductCatalog` dùng `TreeSet<String>` để lưu danh sách category
  không trùng lặp và luôn ở trạng thái đã sắp xếp.
- **Queue**: `OrderQueueManager` dùng `LinkedList` làm `Queue` (FIFO) cho
  trường hợp mọi đơn hàng được đối xử ngang nhau.
- **PriorityQueue**: `OrderQueueManager` dùng để xử lý đơn hàng của khách VIP
  trước, dựa trên `Order implements Comparable<Order>`.

## Khi nào dùng loại nào

| Cần gì | Dùng |
|---|---|
| Tra cứu nhanh theo khóa | `Map` (HashMap nếu không cần thứ tự, TreeMap/LinkedHashMap nếu cần) |
| Tập hợp không trùng lặp | `Set` (HashSet nhanh nhất, TreeSet nếu cần sắp xếp, LinkedHashSet nếu cần giữ thứ tự chèn) |
| Xử lý tuần tự đến trước làm trước | `Queue` (LinkedList/ArrayDeque) |
| Xử lý theo độ ưu tiên, không phải theo thứ tự đến | `PriorityQueue` (cần phần tử implement `Comparable` hoặc truyền `Comparator`) |

Nguyên tắc chung: chọn collection dựa trên **thao tác bạn cần làm nhiều nhất**
(tra cứu? lặp theo thứ tự? loại trùng? lấy phần tử ưu tiên nhất?), không phải
chọn theo thói quen dùng `ArrayList`/`HashMap` cho mọi thứ.
