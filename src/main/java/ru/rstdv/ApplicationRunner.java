package ru.rstdv;

import ru.rstdv.monitoringservice.in.ConsoleApplication;

/**
 * Hello world!
 *
 */
public class ApplicationRunner
{
    private static ConsoleApplication consoleApplication;
    public static void main( String[] args )
    {
        consoleApplication = new ConsoleApplication();
        consoleApplication.start();

    }
}
