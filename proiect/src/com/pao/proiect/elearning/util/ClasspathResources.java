package com.pao.proiect.elearning.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ClasspathResources {

    private ClasspathResources() {
    }

    public static InputStream open(String fileName) throws IOException {
        ClassLoader cl = ClasspathResources.class.getClassLoader();
        InputStream is = cl.getResourceAsStream(fileName);
        if (is != null) {
            return is;
        }
        for (Path path : new Path[]{
                Path.of("resources", fileName),
                Path.of(fileName),
                Path.of("proiect", "resources", fileName),
                Path.of("proiect", fileName)
        }) {
            if (Files.exists(path)) {
                return Files.newInputStream(path);
            }
        }
        throw new IOException("Nu gasesc " + fileName + " (resources/, radacina proiect sau proiect/)");
    }
}
