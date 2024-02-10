package ru.rstdv.monitoringservice.validator;

import lombok.Getter;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;

@Getter
public class CreateUpdateUserDtoValidator implements Validator<CreateUpdateUserDto> {

    private final ValidationResult validationResult;

    public CreateUpdateUserDtoValidator() {
        this.validationResult = new ValidationResult();
    }

    @Override
    public ValidationResult createValidationResult(CreateUpdateUserDto object) {
        if (object.firstname().isBlank()) {
            validationResult.addError("firstname must not be empty");
        }
        if (object.city().isBlank() || !object.city().matches("[a-zA-Z]+")) {
            validationResult.addError("city field can not be empty and must contain only letters");
        }
        if (object.street().isBlank() || !object.street().matches("[a-zA-Z]+")) {
            validationResult.addError("street field can not be empty and must contain only letters");
        }
        if (object.houseNumber().isBlank() || !object.houseNumber().matches("\\d+")) {
            validationResult.addError("house number can not be empty and can not contain letters");
        }
        if (object.personalAccount().isBlank() || !object.personalAccount().matches("\\d{9}")) {
            validationResult.addError("personal account can not be empty and should contain 9 numbers");
        }
        if (object.email().isBlank() || !object.email().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            validationResult.addError("email can not be empty and should satisfy the pattern : %email%@gmail.com");
        }
        if (object.password().isBlank()) {
            validationResult.addError("password can not be empty");
        }
        return validationResult;
    }
}
