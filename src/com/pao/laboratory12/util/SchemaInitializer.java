package com.pao.laboratory12.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaInitializer {

    public static void init(Connection conn) throws SQLException, IOException {
        String url = conn.getMetaData().getURL();
        String schemaFile;

        if (url.contains("mysql")) {
            schemaFile = "schema-mysql.sql";
        } else if (url.contains("sqlite")) {
            schemaFile = "schema-sqlite.sql";
        } else {
            schemaFile = "schema-mysql.sql";
        }

        try (InputStream is = ClasspathResources.open(schemaFile)) {
            String sql = stripComments(new String(is.readAllBytes()));
            for (String stmt : sql.split(";")) {
                String trimmed = stmt.trim();
                if (!trimmed.isEmpty()) {
                    Statement s = conn.createStatement();
                    try {
                        s.execute(trimmed);
                    } finally {
                        s.close();
                    }
                }
            }
        }
        System.out.println("[DB] Schema initializata din " + schemaFile);
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
