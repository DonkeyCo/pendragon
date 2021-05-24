package dev.donkz.pendragon.infrastructure.network.p2p.hive;

import org.apache.commons.io.FileUtils;
import org.hive2hive.core.file.IFileAgent;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class HiveFileAgent implements IFileAgent {

    private final File root;

    public HiveFileAgent() {
        root = new File(FileUtils.getTempDirectory(), UUID.randomUUID().toString());
        root.mkdirs();
    }

    @Override
    public File getRoot() {
        return root;
    }

    @Override
    public void writeCache(String s, byte[] bytes) throws IOException {
    }

    @Override
    public byte[] readCache(String s) throws IOException {
        return new byte[0];
    }
}
