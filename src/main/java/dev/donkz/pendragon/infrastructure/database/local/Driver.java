package dev.donkz.pendragon.infrastructure.database.local;

import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;

import java.util.List;

/**
 * Interface for data driver
 */
public interface Driver {
    /**
     * Save content to repository
     * @param repository repository name
     * @param id id of object
     * @param content object to save
     * @throws IndexAlreadyExistsException
     */
    void save(String repository, String id, Object content) throws IndexAlreadyExistsException;

    /**
     * Search for entity via custom index
     * @param repository repository name
     * @param name index name
     * @param id id of object
     */
    void customIndex(String repository, String name, String id);

    /**
     * Delete entity from repository
     * @param repository repository name
     * @param id id of object
     * @throws EntityNotFoundException
     */
    void delete(String repository, String id) throws EntityNotFoundException;

    /**
     * Update entity in repository
     * @param repository repository name
     * @param id id of object
     * @param entity entity object
     * @param <T> generic type
     * @return found object
     * @throws EntityNotFoundException
     */
    <T> T update(String repository, String id, T entity) throws EntityNotFoundException;

    /**
     * Select all entities for repository and cast them to given type
     * @param repository repository name
     * @param classType cast type
     * @param <T> generic type
     * @return list of objects
     */
    <T> List<T> select(String repository, Class<T> classType);

    /**
     * Select entities for a custom index
     * @param repository repository name
     * @param name index name
     * @param classType type of class
     * @param <T> generic type
     * @return list of objects
     */
    <T> List<T> selectCustomIndex(String repository, String name, Class<T> classType);

    /**
     * Select entity by index
     * @param repository repository name
     * @param id id of object
     * @param classType type of class
     * @param <T> generic type
     * @return object
     * @throws EntityNotFoundException
     */
    <T> T selectByIndex(String repository, String id, Class<T> classType) throws EntityNotFoundException;
}
