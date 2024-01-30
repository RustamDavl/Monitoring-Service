package ru.rstdv.monitoringservice.exception;

/**
 * исключение, которое выбрасывается при ненайденном показании
 */
public class MeterReadingNotFoundException extends RuntimeException {
    public MeterReadingNotFoundException(String message) {
        super(message);
    }
}
