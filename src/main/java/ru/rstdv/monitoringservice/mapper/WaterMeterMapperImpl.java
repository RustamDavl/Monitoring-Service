package ru.rstdv.monitoringservice.mapper;


import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;
import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;

public class WaterMeterMapperImpl implements WaterMeterMapper {
    @Override
    public ReadWaterMeterReadingDto toReadWaterMeterReadingDto(WaterMeterReading waterMeterReading) {
        return new ReadWaterMeterReadingDto(
                waterMeterReading.getId().toString(),
                waterMeterReading.getUserId().toString(),
                waterMeterReading.getColdWater().toString(),
                waterMeterReading.getHotWater().toString(),
                waterMeterReading.getMeterReadingDate()
        );
    }

    @Override
    public WaterMeterReading toWaterMeterReading(CreateUpdateWaterMeterReadingDto createUpdateWaterMeterReadingDto, User user) {
        return WaterMeterReading.builder()
                .userId(user.getId())
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
