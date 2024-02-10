package ru.rstdv.monitoringservice.util;

import lombok.experimental.UtilityClass;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


@UtilityClass
public class PropertyUtil {
    private final Properties PROPERTIES = new Properties();

    static {
        loadDriver();
        loadProperties();
    }

    public static String getPropertyByKey(String key) {
        return PROPERTIES.getProperty(key);
    }

    private void loadProperties() {
        try (InputStream inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
