package ru.rstdv.monitoringservice.validator;

public interface Validator<T> {
    ValidationResult createValidationResult(T object);
}
