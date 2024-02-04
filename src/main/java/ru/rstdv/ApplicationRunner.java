package ru.rstdv;

import ru.rstdv.monitoringservice.in.ConsoleApplication;
import ru.rstdv.monitoringservice.util.CommonConnectionProvider;
import ru.rstdv.monitoringservice.util.LiquibaseUtil;

/**
 * Главный класс приложения.
 */
public class ApplicationRunner {
    public static void main(String[] args) {
        //new LiquibaseUtil(new CommonConnectionProvider()).start();
        LiquibaseUtil.start(new CommonConnectionProvider());
//        ConsoleApplication consoleApplication = new ConsoleApplication();
//        consoleApplication.start();
    }
}
