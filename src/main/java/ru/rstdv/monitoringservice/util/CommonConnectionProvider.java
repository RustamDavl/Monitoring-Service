package ru.rstdv.monitoringservice.util;

import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class CommonConnectionProvider implements ConnectionProvider {
    private final String PASSWORD_KEY = "db.password";
    private final String URL_KEY = "db.url";
    private final String USERNAME_KEY = "db.username";
    private final String SCHEMANAME_KEY = "db.defaultSchema";

    public Connection getConnection() {
        Properties properties = new Properties();
        properties.setProperty("user", PropertyUtil.getPropertyByKey(USERNAME_KEY));
        properties.setProperty("password", PropertyUtil.getPropertyByKey(PASSWORD_KEY));
        properties.setProperty("currentSchema", PropertyUtil.getPropertyByKey(SCHEMANAME_KEY));
        try {
            return DriverManager.getConnection(
                    PropertyUtil.getPropertyByKey(URL_KEY),
                    properties
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
