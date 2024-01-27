package ru.rstdv.monitoringservice.mapper;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.entity.User;

public class ThermalMeterMapperImpl implements ThermalMeterMapper {

    private static final ThermalMeterMapperImpl INSTANCE = new ThermalMeterMapperImpl();

    private ThermalMeterMapperImpl() {
    }

    public static ThermalMeterMapperImpl getInstance() {
        return INSTANCE;
    }

    private final UserMapperImpl userMapperImpl = UserMapperImpl.getInstance();

    @Override
    public ReadThermalMeterReadingDto toReadThermalMeterReadingDto(ThermalMeterReading thermalMeterReading) {
        return new ReadThermalMeterReadingDto(
                thermalMeterReading.getId().toString(),
                userMapperImpl.toReadUserDto(
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
                .gigaCalories(Float.valueOf(createUpdateThermalMeterReadingDto.gigaCalories()))
                .build();
    }
}
