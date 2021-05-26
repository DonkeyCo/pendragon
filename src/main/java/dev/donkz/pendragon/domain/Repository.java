package dev.donkz.pendragon.domain;

import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.SessionAlreadyExists;

import java.util.List;

public interface Repository<T> {
    void save(T entity) throws IndexAlreadyExistsException, SessionAlreadyExists;
    void delete(String id) throws EntityNotFoundException;
    void update(String id, T entity) throws EntityNotFoundException;
    List<T> findAll();
    T findById(String id) throws EntityNotFoundException;
}
