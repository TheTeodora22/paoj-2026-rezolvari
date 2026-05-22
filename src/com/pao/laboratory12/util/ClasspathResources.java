package com.pao.laboratory12.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ClasspathResources {

    private static final String RESOURCES_PREFIX = "com/pao/laboratory12/resources/";
    private static final Path RESOURCES_DIR =
            Path.of("src", "com", "pao", "laboratory12", "resources");

    private ClasspathResources() {
    }

    public static InputStream open(String fileName) throws IOException {
        ClassLoader cl = ClasspathResources.class.getClassLoader();
        InputStream is = cl.getResourceAsStream(fileName);
        if (is == null) {
            is = cl.getResourceAsStream(RESOURCES_PREFIX + fileName);
        }
        if (is != null) {
            return is;
        }
        Path file = RESOURCES_DIR.resolve(fileName);
        if (Files.exists(file)) {
            return Files.newInputStream(file);
        }
        throw new IOException("Nu gasesc " + fileName + " in resources/");
    }
}
