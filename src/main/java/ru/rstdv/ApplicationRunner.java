package ru.rstdv;

import ru.rstdv.monitoringservice.util.CommonConnectionProvider;
import ru.rstdv.monitoringservice.util.LiquibaseUtil;

/**
 * Главный класс приложения.
 */
public class ApplicationRunner {
    public static void main(String[] args) {

        LiquibaseUtil.start(new CommonConnectionProvider());
    }
}
