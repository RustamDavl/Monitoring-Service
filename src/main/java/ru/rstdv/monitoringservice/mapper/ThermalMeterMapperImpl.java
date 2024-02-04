package ru.rstdv.monitoringservice.mapper;

import lombok.RequiredArgsConstructor;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;

import java.time.*;

public class ThermalMeterMapperImpl implements ThermalMeterMapper {

    @Override
    public ReadThermalMeterReadingDto toReadThermalMeterReadingDto(ThermalMeterReading thermalMeterReading) {
        return new ReadThermalMeterReadingDto(
                thermalMeterReading.getId().toString(),
                thermalMeterReading.getUserId().toString(),
                thermalMeterReading.getGigaCalories().toString(),
                thermalMeterReading.getMeterReadingDate()
        );
    }

    @Override
    public ThermalMeterReading toThermalMeterReading(CreateUpdateThermalMeterReadingDto createUpdateThermalMeterReadingDto, User user) {
        return ThermalMeterReading.builder()
                .userId(user.getId())
                .meterReadingDate(MeterReadingDate.builder()
                        .year(Year.now())
                        .month(LocalDate.now().getMonthValue())
                        .monthDay(MonthDay.now().getDayOfMonth())
                        .build())
                .gigaCalories(Float.valueOf(createUpdateThermalMeterReadingDto.gigaCalories()))
                .build();
    }
}
