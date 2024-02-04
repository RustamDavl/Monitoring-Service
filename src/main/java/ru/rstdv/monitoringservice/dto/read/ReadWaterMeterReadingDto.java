package ru.rstdv.monitoringservice.dto.read;

import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;

/**
 * отображение класса WaterMeterReading, возвращаемое пользователю
 *
 * @param id                 идентификатор
 * @param userId        отображение пользователя
 * @param coldWater          значение холодной воды в кубометрах
 * @param hotWater           значение горячей воды в кубометрах
 * @param dateOfMeterReading дата внесения показания
 * @author RustamD
 * @version 1.0
 */
public record ReadWaterMeterReadingDto(
        String id,
        String userId,
        String coldWater,
        String hotWater,
        MeterReadingDate dateOfMeterReading

) {
}
