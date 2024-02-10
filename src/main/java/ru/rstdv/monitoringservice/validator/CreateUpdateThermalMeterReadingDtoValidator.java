package ru.rstdv.monitoringservice.validator;

import lombok.Getter;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;

@Getter
public class CreateUpdateThermalMeterReadingDtoValidator implements Validator<CreateUpdateThermalMeterReadingDto> {
    private final ValidationResult validationResult;

    public CreateUpdateThermalMeterReadingDtoValidator() {
        this.validationResult = new ValidationResult();
    }

    @Override
    public ValidationResult createValidationResult(CreateUpdateThermalMeterReadingDto object) {
        if (!object.gigaCalories().isBlank() && !object.gigaCalories().matches("\\d+\\.\\d{2}")) {
            validationResult.addError("value of giga calories must not be empty and must be like : " + "1234.56");
        }
        return validationResult;
    }
}
