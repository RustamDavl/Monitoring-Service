package ru.rstdv.monitoringservice.mapper;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.entity.User;

public interface ThermalMeterMapper {
    ReadThermalMeterReadingDto toReadWaterMeterReadingDto(ThermalMeterReading thermalMeterReading);


    ThermalMeterReading toThermalMeterReading(CreateUpdateThermalMeterReadingDto createUpdateThermalMeterReadingDto, User user);
}
