package ru.rstdv.monitoringservice.unit.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.Role;
import ru.rstdv.monitoringservice.mapper.ThermalMeterMapper;
import ru.rstdv.monitoringservice.mapper.ThermalMeterMapperImpl;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ThermalMeterMapperTest {

    private final ThermalMeterMapper thermalMeterMapperImpl = ThermalMeterMapperImpl.getInstance();

    @Test
    void toReadThermalMeterReadingDto() {
        var thermalReadingDate = LocalDateTime.now();
        var thermalReading = ThermalMeterReading.builder()
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
                .gigaCalories(123.4F)
                .build();

        var actualResult = thermalMeterMapperImpl.toReadThermalMeterReadingDto(thermalReading);
        var expectedResult = new ReadThermalMeterReadingDto(
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
                "123.4",
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
        var createUpdateThermalMeterReadingDto = new CreateUpdateThermalMeterReadingDto(
                "1",
                "123.4"
        );
        var actualResult = thermalMeterMapperImpl.toThermalMeterReading(createUpdateThermalMeterReadingDto, user);

        var expectedResult = ThermalMeterReading.builder()
                .user(user)
                .gigaCalories(Float.valueOf("123.4"))
                .build();
        assertThat(actualResult.getUser()).isEqualTo(expectedResult.getUser());
        assertThat(actualResult.getGigaCalories()).isEqualTo(expectedResult.getGigaCalories());


    }
}
