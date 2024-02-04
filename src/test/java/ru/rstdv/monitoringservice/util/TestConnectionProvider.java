package ru.rstdv.monitoringservice.util;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RequiredArgsConstructor
public class TestConnectionProvider implements ConnectionProvider {

    private final String url;
    private final String password;
    private final String username;

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
