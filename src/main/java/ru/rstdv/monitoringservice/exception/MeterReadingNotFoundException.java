package ru.rstdv.monitoringservice.exception;

/**
 * исключение, которое выбрасывается когда не уалось найти показание счетчика
 */
public class MeterReadingNotFoundException extends RuntimeException {
    public MeterReadingNotFoundException(String message) {
        super(message);
    }
}
