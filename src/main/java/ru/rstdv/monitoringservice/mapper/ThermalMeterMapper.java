package ru.rstdv.monitoringservice.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.entity.MeterReading;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;
import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;

import java.io.IOException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;

/**
 * интерфейс ThermalMeterMapper необходим для маппинга сущностей
 *
 * @author RustamD
 * @version 1.0
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ThermalMeterMapper {

    /**
     * маппит объект типа ThermalMeterReading в ReadThermalMeterReadingDto, который передается пользователю
     *
     * @param thermalMeterReading показание счетчика тепла
     * @return показание счетчика тепла, передаваемое пользователю
     */

    @Mapping(target = "dateOfMeterReading", source = "meterReadingDate")
    ReadThermalMeterReadingDto toReadThermalMeterReadingDto(ThermalMeterReading thermalMeterReading);

    /**
     * маппит объект типа CreateUpdateThermalMeterReadingDto в ThermalMeterReading, который сохраняется в базу
     *
     * @param createUpdateThermalMeterReadingDto созданное показание
     * @return сохраняемое показание
     */

    default ThermalMeterReading toThermalMeterReading(CreateUpdateThermalMeterReadingDto createUpdateThermalMeterReadingDto) {
        return ThermalMeterReading.builder()
                .userId(Long.valueOf(createUpdateThermalMeterReadingDto.userId()))
                .gigaCalories(Float.valueOf(createUpdateThermalMeterReadingDto.gigaCalories()))
                .meterReadingDate(MeterReadingDate.builder()
                        .year(Year.now())
                        .month(LocalDate.now().getMonthValue())
                        .monthDay(MonthDay.now().getDayOfMonth())
                        .build())
                .build();
    }

}
