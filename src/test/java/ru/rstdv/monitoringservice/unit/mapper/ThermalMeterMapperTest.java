package ru.rstdv.monitoringservice.unit.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;

import ru.rstdv.monitoringservice.mapper.AuditMapperImpl;
import ru.rstdv.monitoringservice.mapper.ThermalMeterMapper;
import ru.rstdv.monitoringservice.mapper.ThermalMeterMapperImpl;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = ThermalMeterMapperImpl.class)
@ExtendWith(SpringExtension.class)
public class ThermalMeterMapperTest {

    @Autowired
    private ThermalMeterMapper thermalMeterMapper;


    @Test
    void toReadThermalMeterReadingDto() {
        var thermalReading = ThermalMeterReading.builder()
                .id(1L)
                .userId(1L)
                .meterReadingDate(MeterReadingDate.builder()
                        .year(Year.now())
                        .month(1)
                        .monthDay(23)
                        .build())
                .gigaCalories(123.4F)
                .build();

        var actualResult = thermalMeterMapper.toReadThermalMeterReadingDto(thermalReading);

        assertThat(actualResult.id()).isEqualTo(thermalReading.getId().toString());
        assertThat(actualResult.userId()).isEqualTo(thermalReading.getUserId().toString());
        assertThat(actualResult.dateOfMeterReading()).isEqualTo(thermalReading.getMeterReadingDate());
        assertThat(actualResult.gigaCalories()).isEqualTo(thermalReading.getGigaCalories().toString());
    }

    @Test
    void toThermalReading() {
        var createUpdateThermalMeterReadingDto = new CreateUpdateThermalMeterReadingDto(
                "1",
                "123.4"
        );
        var actualResult = thermalMeterMapper.toThermalMeterReading(createUpdateThermalMeterReadingDto);

        assertThat(actualResult.getUserId()).isEqualTo(Long.valueOf(createUpdateThermalMeterReadingDto.userId()));
        assertThat(actualResult.getGigaCalories()).isEqualTo(Float.valueOf(createUpdateThermalMeterReadingDto.gigaCalories()));


    }
}
