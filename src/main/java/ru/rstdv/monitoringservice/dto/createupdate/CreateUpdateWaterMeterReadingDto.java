package ru.rstdv.monitoringservice.dto.createupdate;

/**
 * класс, представляющий запрос на создание показания счетчика воды
 *
 * @param userId    идентификатор пользователя
 * @param coldWater показание холодной воды
 * @param hotWater  показание теплой воды
 * @author RustamD
 * @version 1.0
 */
public record CreateUpdateWaterMeterReadingDto(String userId, String coldWater, String hotWater) {
}
