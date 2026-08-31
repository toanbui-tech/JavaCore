package com.javacore.ordermanagement.generics;

import java.util.List;
import java.util.Optional;

/**
 * GENERICS: <T, ID> la kieu du lieu tham so hoa (type parameter). Cung mot
 * interface Repository nay co the dung cho Product (ID=String), Customer
 * (ID=String), Order (ID=String)... ma khong can viet lai code, va van duoc
 * kiem tra kieu du lieu (type-safe) o thoi diem bien dich thay vi phai ep
 * kieu (cast) thu cong nhu khi dung Object.
 *
 * @param <T>  kieu cua entity duoc luu tru
 * @param <ID> kieu cua khoa chinh (id) dung de tra cuu
 */
public interface Repository<T, ID> {

    T save(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    boolean deleteById(ID id);
}
