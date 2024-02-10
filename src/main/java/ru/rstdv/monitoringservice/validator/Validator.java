package ru.rstdv.monitoringservice.validator;

import java.util.List;

public interface Validator<T> {
     ValidationResult createValidationResult(T object);
}
