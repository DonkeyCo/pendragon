package dev.donkz.pendragon.infrastructure.database.local;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.util.FileHandler;
import dev.donkz.pendragon.util.JSONUtility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class LocalDriver implements Driver {
    private static final Logger LOGGER = Logger.getLogger(LocalDriver.class.getName());
    private static final String DATA_PATH = "data";
    private static final String FILE_TYPE = ".json";

    private final JSONUtility jsonUtil;
    private final FileHandler fileHandler;

    public LocalDriver() {
        this.jsonUtil = new JSONUtility();
        this.fileHandler = new FileHandler();
    }

    public void save(String repository, String id, Object object) throws IndexAlreadyExistsException {
        Path dirPath = Paths.get(DATA_PATH, repository);
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String filename = UUID.randomUUID().toString() + FILE_TYPE;
        Path filePath = Paths.get(DATA_PATH, repository, filename);

        String content = jsonUtil.object2Json(object);

        if (exists(repository, id)) {
            throw new IndexAlreadyExistsException();
        }
        fileHandler.write2File(filePath, content);
        indexObject(id, filename, repository);
    }

    public void customIndex(String repository, String name, String id) {
        Path dirPath = Paths.get(DATA_PATH, repository);
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Path filePath = Paths.get(DATA_PATH, repository, name + ".json");
        String mappedFilename = getMappedFile(repository, id);
        String content = String.format("{\"%s\": \"%s\"}", id, mappedFilename);
        fileHandler.write2File(filePath, content);
    }

    public void delete(String repository, String id) throws EntityNotFoundException {
        Path filePath = Paths.get(DATA_PATH, repository, "index.json");
        if (Files.exists(filePath)) {
            ObjectNode indices = jsonUtil.getObjectNode(filePath.toFile());
            Path objectPath = Paths.get(DATA_PATH, repository, indices.get(id).textValue());
            indices.remove(id);
            fileHandler.write2File(filePath, indices.toString());
            if (Files.exists(objectPath)) {
                try {
                    Files.delete(objectPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new EntityNotFoundException();
            }
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public <T> T update(String repository, String id, T entity) throws EntityNotFoundException {
        Path filePath = Paths.get(DATA_PATH, repository, "index.json");
        if (Files.exists(filePath)) {
            ObjectNode indices = jsonUtil.getObjectNode(filePath.toFile());
            Path objectPath = Paths.get(DATA_PATH, repository, indices.get(id).textValue());
            if (Files.exists(objectPath)) {
                fileHandler.write2File(objectPath, jsonUtil.object2Json(entity));
                return entity;
            }
        }
        throw new EntityNotFoundException();
    }

    public <T> List<T> select(String repository, Class<T> classType) {
        Path filePath = Paths.get(DATA_PATH, repository, "index.json");
        return selectByIndices(filePath, repository, classType);
    }

    @Override
    public <T> List<T> selectCustomIndex(String repository, String name, Class<T> classType) {
        Path filePath = Paths.get(DATA_PATH, repository, name + ".json");
        return selectByIndices(filePath, repository, classType);
    }

    @Override
    public <T> T selectByIndex(String repository, String id, Class<T> classType) throws EntityNotFoundException {
        Path filePath = Paths.get(DATA_PATH, repository, "index.json");
        if (Files.exists(filePath)) {
            ObjectNode indices = jsonUtil.getObjectNode(filePath.toFile());
            JsonNode node = indices.get(id);
            if (node != null) {
                Path objectPath = Paths.get(DATA_PATH, repository, node.textValue());
                if (Files.exists(objectPath)) {
                    return jsonUtil.json2Object(objectPath.toFile(), classType);
                }
            }
        }
        throw new EntityNotFoundException();
    }

    private void indexObject(String id, String fileName, String repository) {
        Path filePath = Paths.get(DATA_PATH, repository, "index.json");
        String content = "";
        if (Files.exists(filePath)) {
            ObjectNode indices = jsonUtil.getObjectNode(filePath.toFile());
            indices.put(id, fileName);
            content = indices.toString();
        } else {
            content = String.format("{\"%s\": \"%s\"}", id, fileName);
        }
        fileHandler.write2File(filePath, content);
    }

    public boolean exists(String repository, String id) {
        Path filePath = Paths.get(DATA_PATH, repository, "index.json");
        if (Files.exists(filePath)) {
            ObjectNode indices = jsonUtil.getObjectNode(filePath.toFile());
            return indices.get(id) != null;
        }
        return false;
    }

    private String getMappedFile(String repository, String id) {
        Path filePath = Paths.get(DATA_PATH, repository, "index.json");
        if (Files.exists(filePath)) {
            ObjectNode indices = jsonUtil.getObjectNode(filePath.toFile());
            return indices.get(id).textValue();
        }
        return null;
    }

    private <T> List<T> selectByIndices(Path filePath, String repository, Class<T> classType) {
        List<T> objects = new ArrayList<>();
        if (Files.exists(filePath)) {
            ObjectNode indices = jsonUtil.getObjectNode(filePath.toFile());
            for (JsonNode index : indices) {
                Path objectPath = Paths.get(DATA_PATH, repository, index.textValue());
                if (Files.exists(objectPath)) {
                    objects.add(jsonUtil.json2Object(objectPath.toFile(), classType));
                }
            }
        }
        return objects;
    }
}
