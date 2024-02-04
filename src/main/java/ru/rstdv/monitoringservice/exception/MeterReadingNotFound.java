package ru.rstdv.monitoringservice.exception;

/**
 * исключение, которое выбрасывается при ненайденном показании
 */
public class MeterReadingNotFound extends RuntimeException {
    public MeterReadingNotFound(String message) {
        super(message);
    }
}
