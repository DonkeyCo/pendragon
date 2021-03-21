package dev.donkz.pendragon.infrastructure.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;

import java.util.List;

public interface Driver {
    void save(String repository, String id, Object content) throws IndexAlreadyExistsException;
    void delete(String repository, String id) throws EntityNotFoundException;
    <T> T update(String repository, String id, T entity) throws EntityNotFoundException;
    <T> List<T> select(String repository, Class<T> classType);
    <T> T selectByIndex(String repository, String id, Class<T> classType) throws EntityNotFoundException;
}
