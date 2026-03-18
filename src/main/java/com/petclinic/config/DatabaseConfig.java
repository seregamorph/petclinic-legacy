package com.petclinic.config;

import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton that holds database configuration and provides JDBC connections.
 * Also responsible for running Flyway migrations on first access.
 */
public class DatabaseConfig {

    private static DatabaseConfig instance;

    private final String url;
    private final String username;
    private final String password;

    private DatabaseConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            Properties props = new Properties();
            try (InputStream in = DatabaseConfig.class
                    .getClassLoader().getResourceAsStream("db.properties")) {
                if (in == null) {
                    throw new IllegalStateException("db.properties not found on classpath");
                }
                props.load(in);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load db.properties", e);
            }

            // Allow environment-variable overrides
            String url      = System.getenv().getOrDefault("DB_URL",      props.getProperty("db.url"));
            String username = System.getenv().getOrDefault("DB_USERNAME",  props.getProperty("db.username"));
            String password = System.getenv().getOrDefault("DB_PASSWORD",  props.getProperty("db.password"));

            instance = new DatabaseConfig(url, username, password);
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void runMigrations() {
        Flyway flyway = Flyway.configure()
                .dataSource(url, username, password)
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
    }
}
