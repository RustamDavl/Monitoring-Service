package ru.rstdv.monitoringservice.unit.mapper;

import org.junit.jupiter.api.Test;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.Role;
import ru.rstdv.monitoringservice.mapper.WaterMeterMapper;
import ru.rstdv.monitoringservice.mapper.WaterMeterMapperImpl;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class WaterMeterMapperTest {

    private final WaterMeterMapper waterMeterMapperImpl = WaterMeterMapperImpl.getInstance();

    @Test
    void toReadWaterMeterReadingDto() {
        var thermalReadingDate = LocalDateTime.now();
        var waterReading = WaterMeterReading.builder()
                .id(1L)
                .user(User.builder()
                        .id(1L)
                        .firstname("Vi")
                        .email("vivi@gmail.com")
                        .password("pass")
                        .personalAccount("999999999")
                        .address(
                                Address.builder()
                                        .city("Nigh city")
                                        .street("jig-jig")
                                        .houseNumber("1")
                                        .build()
                        )
                        .role(Role.USER)
                        .build())
                .dateOfMeterReading(thermalReadingDate)
                .coldWater(123)
                .hotWater(345)
                .build();

        var actualResult = waterMeterMapperImpl.toReadWaterMeterReadingDto(waterReading);
        var expectedResult = new ReadWaterMeterReadingDto(
                "1",
                new ReadUserDto(
                        "1",
                        "Vi",
                        "vivi@gmail.com",
                        Address.builder()
                                .city("Nigh city")
                                .street("jig-jig")
                                .houseNumber("1")
                                .build(),
                        "USER",
                        "999999999"
                ),
                "123",
                "345",
                thermalReadingDate.toString()
        );
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void toThermalReading() {
        var user = User.builder()
                .id(1L)
                .firstname("Vi")
                .email("vivi@gmail.com")
                .password("pass")
                .personalAccount("999999999")
                .address(
                        Address.builder()
                                .city("Nigh city")
                                .street("jig-jig")
                                .houseNumber("1")
                                .build()
                )
                .role(Role.USER)
                .build();
        var createUpdateWaterMeterReadingDto = new CreateUpdateWaterMeterReadingDto(
                "1",
                "111",
                "222"
        );
        var actualResult = waterMeterMapperImpl.toWaterMeterReading(createUpdateWaterMeterReadingDto, user);

        var expectedResult = WaterMeterReading.builder()
                .user(user)
                .coldWater(111)
                .hotWater(222)
                .build();
        assertThat(actualResult.getUser()).isEqualTo(expectedResult.getUser());
        assertThat(actualResult.getColdWater()).isEqualTo(expectedResult.getColdWater());
        assertThat(actualResult.getHotWater()).isEqualTo(expectedResult.getHotWater());


    }
}
