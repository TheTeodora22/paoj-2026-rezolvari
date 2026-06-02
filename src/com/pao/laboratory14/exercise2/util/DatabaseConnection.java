package com.pao.laboratory14.exercise2.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton care gestioneaza conexiunea la baza de date.
 * Citeste configuratia din db.properties de pe classpath.
 *
 * Configurare IntelliJ: marcheaza 'exercise2/resources/' ca Resources Root.
 */
public class DatabaseConnection {

    private static final Path DB_FILE =
            Path.of("src/com/pao/laboratory14/exercise2/resources/db.properties");

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws IOException, SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Adauga lib/sqlite-jdbc-3.47.1.0.jar la Libraries.", e);
        }

        Properties props = new Properties();
        try (InputStream is = openDbProperties()) {
            props.load(is);
        }
        String url      = props.getProperty("db.url");
        String user     = props.getProperty("db.user", "");
        String password = props.getProperty("db.password", "");
        this.connection = DriverManager.getConnection(url, user, password);
    }

    private InputStream openDbProperties() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties");
        if (is != null) {
            return is;
        }
        if (Files.exists(DB_FILE)) {
            return Files.newInputStream(DB_FILE);
        }
        throw new IOException(
                "db.properties nu a fost gasit. Working directory trebuie sa fie radacina proiectului.");
    }

    public static DatabaseConnection getInstance() throws IOException, SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
