package ru.rstdv.monitoringservice.mapper;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.entity.User;

/**
 * интерфейс ThermalMeterMapper необходим для маппинга сущностей
 *
 * @author RustamD
 * @version 1.0
 */
public interface ThermalMeterMapper {
    /**
     * маппит объект типа ThermalMeterReading в ReadThermalMeterReadingDto, который передается пользователю
     *
     * @param thermalMeterReading показание счетчика тепла
     * @return показание счетчика тепла, передаваемое пользователю
     */
    ReadThermalMeterReadingDto toReadThermalMeterReadingDto(ThermalMeterReading thermalMeterReading);

    /**
     * маппит объект типа CreateUpdateThermalMeterReadingDto в ThermalMeterReading, который сохраняется в базу
     *
     * @param createUpdateThermalMeterReadingDto созданное показание
     * @param user                               пользователь, показание которого сохраняется
     * @return сохраняемое показание
     */
    ThermalMeterReading toThermalMeterReading(CreateUpdateThermalMeterReadingDto createUpdateThermalMeterReadingDto, User user);
}
