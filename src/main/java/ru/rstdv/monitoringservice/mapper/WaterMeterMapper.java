package ru.rstdv.monitoringservice.mapper;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;

/**
 * интерфейс WaterMeterMapper необходим для маппинга сущностей
 *
 * @author RustamD
 * @version 1.0
 */
public interface WaterMeterMapper {

    /**
     * маппит объект типа WaterMeterReading в ReadWaterMeterReadingDto, который передается пользователю
     *
     * @param waterMeterReading показание счетчика воды
     * @return показание счетчика тепла, передаваемое пользователю
     */
    ReadWaterMeterReadingDto toReadWaterMeterReadingDto(WaterMeterReading waterMeterReading);


    /**
     * маппит объект типа CreateUpdateWaterMeterReadingDto в WaterMeterReading, который сохраняется в базу
     *
     * @param createUpdateWaterMeterReadingDto созданное показание
     * @param user                             пользователь, показание которого сохраняется
     * @return сохраняемое показание
     */
    WaterMeterReading toWaterMeterReading(CreateUpdateWaterMeterReadingDto createUpdateWaterMeterReadingDto, User user);
}
