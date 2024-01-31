package ru.rstdv.monitoringservice.util;

import lombok.experimental.UtilityClass;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


@UtilityClass
public class PropertyUtil {

    private final Properties PROPERTIES = new Properties();
    private final String FILE = "./src/main/resources/application.properties";

    static {
        loadProperties();
    }

    public static String getPropertyByKey(String key) {
        return PROPERTIES.getProperty(key);
    }

    private void loadProperties() {
        try (FileReader fileReader = new FileReader(FILE)) {
            PROPERTIES.load(fileReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
