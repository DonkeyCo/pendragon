package dev.donkz.pendragon.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileHandler {
    public String readFile2String(Path filePath) {
        String content = "";
        try {
            content = String.join("", Files.readAllLines(filePath, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public void write2File(Path filePath, String content) {
        try {
            final BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8);
            System.out.println(content);
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
