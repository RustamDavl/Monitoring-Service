package ru.rstdv.monitoringservice.validator;

import lombok.Getter;
import ru.rstdv.monitoringservice.dto.createupdate.UserAuthDto;

@Getter
public class UserAuthDtoValidator implements Validator<UserAuthDto> {

    private final ValidationResult validationResult;

    public UserAuthDtoValidator() {
        this.validationResult = new ValidationResult();
    }
    @Override
    public ValidationResult createValidationResult(UserAuthDto object) {
        if (object.email().isBlank() || !object.email().matches("[a-zA-z]+@gmail.com")) {
            validationResult.addError("email can not be empty and should satisfy the pattern : " + "%email%@gmail.com");
        }
        if (object.password().isBlank()) {
            validationResult.addError("password can not be empty");
        }
        return validationResult;
    }
}
