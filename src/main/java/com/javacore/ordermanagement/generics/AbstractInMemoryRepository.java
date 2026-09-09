package com.javacore.ordermanagement.generics;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * GENERIC ABSTRACT CLASS: cài đặt sẵn phần logic CRUD dùng chung cho mọi
 * Repository, dựa trên Map<ID, T>. Các lớp con (ProductRepository,
 * CustomerRepository...) chỉ cần cung cấp hàm lấy id từ entity - thể hiện
 * TEMPLATE METHOD đơn giản kết hợp với generics.
 * <p>
 * COLLECTIONS: dùng LinkedHashMap thay vì HashMap để giữ THỨ TỰ CHÈN
 * (insertion order) khi duyệt findAll() - giúp kết quả demo/log dễ đọc và
 * ổn định giữa các lần chạy.
 */
public abstract class AbstractInMemoryRepository<T, ID> implements Repository<T, ID> {

    private final Map<ID, T> storage = new LinkedHashMap<>();
    private final Function<T, ID> idExtractor;

    protected AbstractInMemoryRepository(Function<T, ID> idExtractor) {
        this.idExtractor = idExtractor;
    }

    @Override
    public T save(T entity) {
        storage.put(idExtractor.apply(entity), entity);
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean deleteById(ID id) {
        return storage.remove(id) != null;
    }
}
