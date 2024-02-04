package ru.rstdv.monitoringservice.util;

import java.sql.Connection;

/**
 * интерфейс для возможности предоствления различных соединений
 *
 * @author RustamD
 * @version 1.0
 */
public interface ConnectionProvider {
    Connection getConnection();
}
