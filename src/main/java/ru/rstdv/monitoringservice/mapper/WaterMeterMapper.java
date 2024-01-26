package ru.rstdv.monitoringservice.mapper;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;

public interface WaterMeterMapper {


     ReadWaterMeterReadingDto toReadWaterMeterReadingDto(WaterMeterReading waterMeterReading);

     WaterMeterReading toWaterMeterReading(CreateUpdateWaterMeterReadingDto createUpdateWaterMeterReadingDto, User user);
}
