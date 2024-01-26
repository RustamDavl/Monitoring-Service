package ru.rstdv.monitoringservice.dto.read;

public record ReadThermalMeterReadingDto(
        ReadUserDto readUserDto,
        String gigaCalories,

        String dateOfMeterReading

) {
}
