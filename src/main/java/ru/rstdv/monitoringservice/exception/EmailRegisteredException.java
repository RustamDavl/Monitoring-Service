package ru.rstdv.monitoringservice.exception;

/**
 * исключение, которое выбрасывается, если пользователь с данным email уже зарегистрирован
 */
public class EmailRegisteredException extends RuntimeException {
    public EmailRegisteredException(String message) {
        super(message);
    }
}
