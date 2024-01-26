package ru.rstdv.monitoringservice.mapper;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.entity.User;

public class ThermalMeterMapper {

    private static final ThermalMeterMapper INSTANCE = new ThermalMeterMapper();

    private ThermalMeterMapper() {
    }

    public static ThermalMeterMapper getInstance() {
        return INSTANCE;
    }

    private final UserMapper userMapper = UserMapper.getInstance();

    public ReadThermalMeterReadingDto toReadWaterMeterReadingDto(ThermalMeterReading thermalMeterReading) {
        return new ReadThermalMeterReadingDto(
                userMapper.toReadUserDto(
                        thermalMeterReading.getUser()
                ),
                thermalMeterReading.getGigaCalories().toString(),
                thermalMeterReading.getDateOfMeterReading().toString()
        );
    }

    public ThermalMeterReading toThermalMeterReading(CreateUpdateThermalMeterReadingDto createUpdateThermalMeterReadingDto, User user) {
        return ThermalMeterReading.builder()
                .user(
                        user
                )
                .gigaCalories(Float.valueOf(createUpdateThermalMeterReadingDto.gigaCalories()))
                .build();
    }
}
