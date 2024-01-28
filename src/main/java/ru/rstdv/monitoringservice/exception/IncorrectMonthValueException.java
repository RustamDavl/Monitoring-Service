package ru.rstdv.monitoringservice.exception;

public class IncorrectMonthValueException extends RuntimeException {
    public IncorrectMonthValueException(String message) {
        super(message);
    }
}
