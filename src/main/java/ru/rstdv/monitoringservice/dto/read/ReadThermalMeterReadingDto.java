package ru.rstdv.monitoringservice.dto.read;

import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;

/**
 * отображение класса ThermalMeterReading, возвращаемое пользователю
 *
 * @param id                 идентификатор
 * @param userId             идентификатор пользователя
 * @param gigaCalories       значение в Гкал
 * @param dateOfMeterReading дата внесения показания
 * @author RustamD
 * @version 1.0
 */
public record ReadThermalMeterReadingDto(
        String id,
        String userId,
        String gigaCalories,
        MeterReadingDate dateOfMeterReading

) {
}
