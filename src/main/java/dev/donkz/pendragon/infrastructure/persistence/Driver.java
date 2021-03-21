package dev.donkz.pendragon.infrastructure.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;

import java.util.List;

public interface Driver {
    void save(String repository, String id, Object content) throws IndexAlreadyExistsException;
    <T> List<T> select(String repository, TypeReference<T> typeRef);
    <T> T filterBy(String repository, String attrName, String attrVal, TypeReference<T> typeRef);
    <T> T selectByIndex(String repository, String id, TypeReference<T> typeRef);
}
