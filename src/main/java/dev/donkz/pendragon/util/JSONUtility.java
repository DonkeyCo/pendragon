package dev.donkz.pendragon.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class JSONUtility {
    private final ObjectMapper mapper;

    public JSONUtility() {
        this.mapper = new ObjectMapper();
    }

    public <T> T json2Object(File file, Class<T> classType) {
        T object = null;
        try {
            object = this.mapper.readValue(file, classType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    public <T> T json2Object(File file, TypeReference<T> typeRef) {
        T object = null;
        try {
            object = this.mapper.readValue(file, typeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    public <T> T json2Object(String content, Class<T> classType) {
        T object = null;
        try {
            object = this.mapper.readValue(content, classType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    public String object2Json(Object object) {
        String content = "";
        try {
            content = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return content;
    }

    public ObjectNode getObjectNode(File file) {
        ObjectNode node = null;
        try {
            JsonNode jsonNode = mapper.readTree(file);
            node = (ObjectNode) jsonNode;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return node;
    }
}
