package ru.rstdv.monitoringservice.unit.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;
import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;
import ru.rstdv.monitoringservice.mapper.WaterMeterMapper;
import ru.rstdv.monitoringservice.mapper.WaterMeterMapperImpl;

import java.time.LocalDateTime;
import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

public class WaterMeterMapperTestFactory {

    private WaterMeterMapper waterMeterMapper;

    @BeforeEach
    void setUp() {
        waterMeterMapper = new WaterMeterMapperImpl();
    }

    @Test
    void toReadWaterMeterReadingDto() {
        var thermalReadingDate = LocalDateTime.now();
        var waterReading = WaterMeterReading.builder()
                .id(1L)
                .userId(1L)
                .meterReadingDate(MeterReadingDate.builder()
                        .year(Year.now())
                        .month(1)
                        .monthDay(23)
                        .build())
                .coldWater(123)
                .hotWater(345)
                .build();

        var actualResult = waterMeterMapper.toReadWaterMeterReadingDto(waterReading);
        assertThat(actualResult.id()).isEqualTo(waterReading.getId().toString());
        assertThat(actualResult.userId()).isEqualTo(waterReading.getUserId().toString());
        assertThat(actualResult.dateOfMeterReading()).isEqualTo(waterReading.getMeterReadingDate());
        assertThat(actualResult.coldWater()).isEqualTo(waterReading.getColdWater().toString());
        assertThat(actualResult.hotWater()).isEqualTo(waterReading.getHotWater().toString());

    }

    @Test
    void toThermalReading() {
        var user = User.builder()
                .id(1L)
                .build();
        var createUpdateWaterMeterReadingDto = new CreateUpdateWaterMeterReadingDto(
                user.getId().toString(),
                "222",
                "111"
        );
        var actualResult = waterMeterMapper.toWaterMeterReading(createUpdateWaterMeterReadingDto, user);

        assertThat(actualResult.getUserId()).isEqualTo(Long.valueOf(createUpdateWaterMeterReadingDto.userId()));
        assertThat(actualResult.getColdWater()).isEqualTo(Integer.valueOf(createUpdateWaterMeterReadingDto.coldWater()));
        assertThat(actualResult.getHotWater()).isEqualTo(Integer.valueOf(createUpdateWaterMeterReadingDto.hotWater()));


    }
}
