package ru.rstdv.monitoringservice.mapper;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;

public class WaterMeterMapper {

    private static final WaterMeterMapper INSTANCE = new WaterMeterMapper();
    private WaterMeterMapper() {}

    public static WaterMeterMapper getInstance() {
        return INSTANCE;
    }

    private final UserMapper userMapper = UserMapper.getInstance();

    public ReadWaterMeterReadingDto toReadWaterMeterReadingDto(WaterMeterReading waterMeterReading) {
        return new ReadWaterMeterReadingDto(
                userMapper.toReadUserDto(
                        waterMeterReading.getUser()
                ),
                waterMeterReading.getColdWater().toString(),
                waterMeterReading.getHotWater().toString(),
                waterMeterReading.getDateOfMeterReading().toString()
        );
    }

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
