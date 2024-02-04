package ru.rstdv.monitoringservice.exception;

/**
 * исключение, которое выбрасывается когда не удалось найти пользователя
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);

    }
}
