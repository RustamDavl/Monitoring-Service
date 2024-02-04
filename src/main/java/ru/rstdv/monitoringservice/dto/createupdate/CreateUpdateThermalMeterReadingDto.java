package ru.rstdv.monitoringservice.dto.createupdate;

/**
 * класс, представляющий запрос на создание показания счетчика тепла
 *
 * @param userId       идентификатор пользователя
 * @param gigaCalories показание в Гкал
 * @author RustamD
 * @version 1.0
 */
public record CreateUpdateThermalMeterReadingDto(String userId, String gigaCalories) {
}
