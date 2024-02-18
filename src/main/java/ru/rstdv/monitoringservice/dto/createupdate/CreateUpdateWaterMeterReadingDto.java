package ru.rstdv.monitoringservice.dto.createupdate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * класс, представляющий запрос на создание показания счетчика воды
 *
 * @param userId    идентификатор пользователя
 * @param coldWater показание холодной воды
 * @param hotWater  показание теплой воды
 * @author RustamD
 * @version 1.0
 */
public record CreateUpdateWaterMeterReadingDto(String userId,
                                               @NotBlank
                                               @Pattern(regexp = "\\d{1,5}")
                                               String coldWater,
                                               @NotBlank
                                               @Pattern(regexp = "\\d{1,5}")
                                               String hotWater) {
}
