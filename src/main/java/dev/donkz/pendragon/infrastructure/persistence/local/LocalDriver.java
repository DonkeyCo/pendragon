package dev.donkz.pendragon.infrastructure.persistence.local;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.infrastructure.persistence.Driver;
import dev.donkz.pendragon.util.FileHandler;
import dev.donkz.pendragon.util.JSONUtility;

import java.io.IOException;
import java.nio.file.*;
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

    public <T> List<T> select(String repository, TypeReference<T> typeRef) {
        Path filePath = Paths.get(DATA_PATH, repository, "index.json");
        List<T> objects = new ArrayList<>();
        if (Files.exists(filePath)) {
            ObjectNode indices = jsonUtil.getObjectNode(filePath.toFile());
            for(JsonNode index : indices) {
                Path objectPath = Paths.get(DATA_PATH, repository, index.textValue());
                objects.add(jsonUtil.json2Object(objectPath.toFile(), typeRef));
            }
        }
        return objects;
    }

    @Override
    public <T> T filterBy(String repository, String attrName, String attrVal, TypeReference<T> typeRef) {
        return null;
    }

    @Override
    public <T> T selectByIndex(String repository, String id, TypeReference<T> typeRef) {
        Path filePath = Paths.get(DATA_PATH, repository, "index.json");
        if (Files.exists(filePath)) {
            ObjectNode indices = jsonUtil.getObjectNode(filePath.toFile());
            JsonNode node = indices.get(id);
            if (node != null) {
                System.out.println(node.textValue());
            } else {
                return null;
            }
        }
        return null;
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
}
