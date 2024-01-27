package ru.rstdv.monitoringservice.exception;

public class MeterReadingNotFound extends RuntimeException {
    public MeterReadingNotFound(String message) {
        super(message);
    }
}
