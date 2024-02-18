package ru.rstdv.monitoringservice.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;
import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;

import java.io.IOException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;

/**
 * интерфейс WaterMeterMapper необходим для маппинга сущностей
 *
 * @author RustamD
 * @version 1.0
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WaterMeterMapper {

    /**
     * маппит объект типа WaterMeterReading в ReadWaterMeterReadingDto, который передается пользователю
     *
     * @param waterMeterReading показание счетчика воды
     * @return показание счетчика тепла, передаваемое пользователю
     */

    @Mapping(target = "dateOfMeterReading", source = "meterReadingDate")
    ReadWaterMeterReadingDto toReadWaterMeterReadingDto(WaterMeterReading waterMeterReading);


    /**
     * маппит объект типа CreateUpdateWaterMeterReadingDto в WaterMeterReading, который сохраняется в базу
     *
     * @param createUpdateWaterMeterReadingDto созданное показание
     * @return сохраняемое показание
     */

    default WaterMeterReading toWaterMeterReading(CreateUpdateWaterMeterReadingDto createUpdateWaterMeterReadingDto) {
        return WaterMeterReading.builder()
                .userId(Long.valueOf(createUpdateWaterMeterReadingDto.userId()))
                .coldWater(Integer.valueOf(createUpdateWaterMeterReadingDto.coldWater()))
                .hotWater(Integer.valueOf(createUpdateWaterMeterReadingDto.hotWater()))
                .meterReadingDate(MeterReadingDate.builder()
                        .year(Year.now())
                        .month(LocalDate.now().getMonthValue())
                        .monthDay(MonthDay.now().getDayOfMonth())
                        .build())
                .build();
    }
}
