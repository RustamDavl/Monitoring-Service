package ru.rstdv.monitoringservice.dto.read;

/**
 * отображение класса WaterMeterReading, возвращаемое пользователю
 *
 * @param id                 идентификатор
 * @param readUserDto        отображение пользователя
 * @param coldWater          значение холодной воды в кубометрах
 * @param hotWater           значение горячей воды в кубометрах
 * @param dateOfMeterReading дата внесения показания
 */
public record ReadWaterMeterReadingDto(

        String id,
        ReadUserDto readUserDto,
        String coldWater,
        String hotWater,
        String dateOfMeterReading

) {
}
