package ru.rstdv.monitoringservice.mapper;

import lombok.RequiredArgsConstructor;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.entity.User;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class ThermalMeterMapperImpl implements ThermalMeterMapper {

    private final UserMapper userMapper;

    @Override
    public ReadThermalMeterReadingDto toReadThermalMeterReadingDto(ThermalMeterReading thermalMeterReading) {
        return new ReadThermalMeterReadingDto(
                thermalMeterReading.getId().toString(),
                userMapper.toReadUserDto(
                        thermalMeterReading.getUser()
                ),
                thermalMeterReading.getGigaCalories().toString(),
                thermalMeterReading.getDateOfMeterReading().toString()
        );
    }

    @Override
    public ThermalMeterReading toThermalMeterReading(CreateUpdateThermalMeterReadingDto createUpdateThermalMeterReadingDto, User user) {
        return ThermalMeterReading.builder()
                .user(user)
                .dateOfMeterReading(LocalDateTime.now())
                .gigaCalories(Float.valueOf(createUpdateThermalMeterReadingDto.gigaCalories()))
                .build();
    }
}
