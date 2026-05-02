package com.petclinic.config;

import com.petclinic.spring.ApplicationContextHolder;
import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton that holds database configuration and provides JDBC connections.
 * Also responsible for running Flyway migrations on first access.
 */
public class DatabaseConfig {

    private final String url;
    private final String username;
    private final String password;

    public DatabaseConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Deprecated // use dependency injection
    public static DatabaseConfig getInstance() {
        return ApplicationContextHolder.getBean(DatabaseConfig.class);
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
