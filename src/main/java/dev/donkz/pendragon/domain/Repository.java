package dev.donkz.pendragon.domain;

import java.util.List;

public interface Repository<T> {
    void save(T entity);
    void delete(String id);
    void update(String id, T entity);
    List<T> findAll();
    T findById();
}
