package ru.rstdv.monitoringservice.util;

import java.sql.Connection;

public interface ConnectionProvider {
    Connection getConnection();
}
