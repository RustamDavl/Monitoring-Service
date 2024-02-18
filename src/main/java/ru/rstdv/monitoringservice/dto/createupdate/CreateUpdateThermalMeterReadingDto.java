package ru.rstdv.monitoringservice.dto.createupdate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * класс, представляющий запрос на создание показания счетчика тепла
 *
 * @param userId       идентификатор пользователя
 * @param gigaCalories показание в Гкал
 * @author RustamD
 * @version 1.0
 */
public record CreateUpdateThermalMeterReadingDto(String userId,
                                                 @NotBlank
                                                 @Pattern(regexp = "\\d+\\.\\d{2}")
                                                 String gigaCalories) {
}
