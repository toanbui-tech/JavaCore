package com.javacore.ordermanagement.generics;

import java.util.List;
import java.util.Optional;

/**
 * GENERICS: <T, ID> là kiểu dữ liệu tham số hóa (type parameter). Cùng một
 * interface Repository này có thể dùng cho Product (ID=String), Customer
 * (ID=String), Order (ID=String)... mà không cần viết lại code, và vẫn được
 * kiểm tra kiểu dữ liệu (type-safe) ở thời điểm biên dịch thay vì phải ép
 * kiểu (cast) thủ công như khi dùng Object.
 *
 * @param <T>  kiểu của entity được lưu trữ
 * @param <ID> kiểu của khóa chính (id) dùng để tra cứu
 */
public interface Repository<T, ID> {

    T save(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    boolean deleteById(ID id);
}
