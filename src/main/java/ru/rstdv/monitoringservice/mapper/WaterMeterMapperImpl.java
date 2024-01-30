package ru.rstdv.monitoringservice.mapper;

import lombok.RequiredArgsConstructor;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;

@RequiredArgsConstructor
public class WaterMeterMapperImpl implements WaterMeterMapper {

    private final UserMapper userMapper;

    @Override
    public ReadWaterMeterReadingDto toReadWaterMeterReadingDto(WaterMeterReading waterMeterReading) {
        return new ReadWaterMeterReadingDto(
                waterMeterReading.getId().toString(),
                userMapper.toReadUserDto(
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
