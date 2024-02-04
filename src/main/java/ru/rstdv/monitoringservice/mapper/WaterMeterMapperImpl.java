package ru.rstdv.monitoringservice.mapper;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;

public class WaterMeterMapperImpl implements WaterMeterMapper {

    private static final WaterMeterMapperImpl INSTANCE = new WaterMeterMapperImpl();

    private WaterMeterMapperImpl() {
    }

    public static WaterMeterMapperImpl getInstance() {
        return INSTANCE;
    }

    private final UserMapperImpl userMapperImpl = UserMapperImpl.getInstance();

    @Override
    public ReadWaterMeterReadingDto toReadWaterMeterReadingDto(WaterMeterReading waterMeterReading) {
        return new ReadWaterMeterReadingDto(
                waterMeterReading.getId().toString(),
                userMapperImpl.toReadUserDto(
                        waterMeterReading.getUser()
                ),
                waterMeterReading.getColdWater().toString(),
                waterMeterReading.getHotWater().toString(),
                waterMeterReading.getDateOfMeterReading().toString()
        );
    }

    @Override
    public WaterMeterReading toWaterMeterReading(CreateUpdateWaterMeterReadingDto createUpdateWaterMeterReadingDto, User user) {
        return WaterMeterReading.builder()
                .user(
                        user
                )
                .coldWater(Integer.valueOf(createUpdateWaterMeterReadingDto.coldWater()))
                .hotWater(Integer.valueOf(createUpdateWaterMeterReadingDto.hotWater()))
                .build();
    }
}
