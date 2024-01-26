package ru.rstdv.monitoringservice.dto.read;

public record ReadWaterMeterReadingDto(

        String id,
        ReadUserDto readUserDto,
        String coldWater,
        String hotWater,
        String dateOfMeterReading

) {
}
