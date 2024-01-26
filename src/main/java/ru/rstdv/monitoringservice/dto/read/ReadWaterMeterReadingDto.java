package ru.rstdv.monitoringservice.dto.read;

import ru.rstdv.monitoringservice.entity.embeddable.Address;

public record ReadWaterMeterReadingDto(
        ReadUserDto readUserDto,
        String coldWater,
        String hotWater,
        String dateOfMeterReading

) {
}
