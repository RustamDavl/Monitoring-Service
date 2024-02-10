package ru.rstdv.monitoringservice.validator;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationResult {
    private final List<String> errors;

    public ValidationResult() {
        this.errors = new ArrayList<>();
    }

    public void addError(String error) {
        this.errors.add(error);
    }
    public boolean isValid() {
        return errors.isEmpty();
    }
}
