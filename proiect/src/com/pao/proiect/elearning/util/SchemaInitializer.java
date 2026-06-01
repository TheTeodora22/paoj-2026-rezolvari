package com.pao.proiect.elearning.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaInitializer {

    public static void init(Connection conn) throws SQLException, IOException {
        try (InputStream is = ClasspathResources.open("schema.sql")) {
            String sql = stripComments(new String(is.readAllBytes()));
            for (String stmt : sql.split(";")) {
                String trimmed = stmt.trim();
                if (!trimmed.isEmpty()) {
                    try (Statement s = conn.createStatement()) {
                        s.execute(trimmed);
                    }
                }
            }
        }
        System.out.println("[DB] Schema initializata");
    }

    private static String stripComments(String sql) {
        StringBuilder sb = new StringBuilder();
        for (String line : sql.split("\n")) {
            String t = line.trim();
            if (!t.startsWith("--") && !t.isEmpty()) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }
}
