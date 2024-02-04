package ru.rstdv.monitoringservice.util;

import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class CommonConnectionProvider implements ConnectionProvider {
    private final String PASSWORD_KEY = "db.password";
    private final String URL_KEY = "db.url";
    private final String USERNAME_KEY = "db.username";

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    PropertyUtil.getPropertyByKey(URL_KEY),
                    PropertyUtil.getPropertyByKey(USERNAME_KEY),
                    PropertyUtil.getPropertyByKey(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
