package dev.donkz.pendragon.infrastructure.persistence.local;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.logging.Logger;

public class FileHandler {
    private static final Logger LOGGER = Logger.getLogger(FileHandler.class.getName());
    private static final String DATA_PATH = "data";

    public void save(String repository, String content) {
        Path dirPath = Paths.get(DATA_PATH);
        try {
            Files.createDirectory(dirPath);
        } catch (FileAlreadyExistsException e) {
            LOGGER.warning(DATA_PATH + " already exists.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path filePath = Paths.get(DATA_PATH, repository + ".json");
        try {
            final BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read(String repository) {
        Path filePath = Paths.get(DATA_PATH, repository + ".json");
        String content = "";
        try {
              content = String.join("", Files.readAllLines(filePath, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
