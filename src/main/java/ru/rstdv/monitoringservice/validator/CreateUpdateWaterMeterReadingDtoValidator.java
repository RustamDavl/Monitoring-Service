package ru.rstdv.monitoringservice.validator;

import lombok.Getter;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;

@Getter
public class CreateUpdateWaterMeterReadingDtoValidator implements Validator<CreateUpdateWaterMeterReadingDto> {
    private final ValidationResult validationResult;

    public CreateUpdateWaterMeterReadingDtoValidator() {
        this.validationResult = new ValidationResult();
    }

    @Override
    public ValidationResult createValidationResult(CreateUpdateWaterMeterReadingDto object) {
        if (object.coldWater().isBlank() || !object.coldWater().matches("\\d{1,5}")) {
            validationResult.addError("value of cold water must not be empty and must contain only numbers");
        }
        if (object.hotWater().isBlank() || !object.hotWater().matches("\\d{1,5}")) {
            validationResult.addError("value of hot water must not be empty and must contain only numbers");
        }
        return validationResult;
    }
}
