package com.javacore.ordermanagement.generics;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * GENERIC ABSTRACT CLASS: cai dat san phan logic CRUD dung chung cho moi
 * Repository, dua tren Map<ID, T>. Cac lop con (ProductRepository,
 * CustomerRepository...) chi can cung cap ham lay id tu entity - the hien
 * TEMPLATE METHOD don gian ket hop voi generics.
 * <p>
 * COLLECTIONS: dung LinkedHashMap thay vi HashMap de giu THU TU CHEN
 * (insertion order) khi duyet findAll() - giup ket qua demo/log de doc va
 * on dinh giua cac lan chay.
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
