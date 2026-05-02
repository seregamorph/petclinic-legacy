package com.petclinic.spring;

import com.petclinic.config.DatabaseConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class DatabaseConfigConfiguration {
    @Bean
    public DatabaseConfig databaseConfig() {
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
        String url = System.getenv().getOrDefault("DB_URL", props.getProperty("db.url"));
        String username = System.getenv().getOrDefault("DB_USERNAME", props.getProperty("db.username"));
        String password = System.getenv().getOrDefault("DB_PASSWORD", props.getProperty("db.password"));

        return new DatabaseConfig(url, username, password);
    }
}
