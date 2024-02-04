package ru.rstdv;

import ru.rstdv.monitoringservice.in.ConsoleApplication;

/**
 * Главный класс приложения.
 */
public class ApplicationRunner {

    public static void main(String[] args) {
        ConsoleApplication consoleApplication = new ConsoleApplication();
        consoleApplication.start();

    }
}
