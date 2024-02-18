package ru.rstdv.monitoringservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


@Component
@RequiredArgsConstructor
public class CommonConnectionProvider implements DataSource {

    @Value("${db.url}")
    private final String url;
    @Value("${db.password}")
    private final String password;
    @Value("${db.username}")
    private final String username;
    @Value("${db.defaultSchema}")
    private final String defaultSchema;
    private Properties properties;

    static {
        loadDriver();
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, createProperties());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Properties createProperties() {
        properties = new Properties();
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        properties.setProperty("currentSchema", defaultSchema);
        return properties;
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
