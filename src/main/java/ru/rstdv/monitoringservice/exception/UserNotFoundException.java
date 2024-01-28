package ru.rstdv.monitoringservice.exception;

/**
 * исключение, которое выбрасывается при ненайденном пользователе
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);

    }
}
