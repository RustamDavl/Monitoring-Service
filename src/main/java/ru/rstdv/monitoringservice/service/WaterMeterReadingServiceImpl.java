package ru.rstdv.monitoringservice.service;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilter;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.WaterMeterMapper;
import ru.rstdv.monitoringservice.mapper.WaterMeterMapperImpl;
import ru.rstdv.monitoringservice.repository.MeterReadingRepository;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.repository.UserRepositoryImpl;
import ru.rstdv.monitoringservice.repository.WaterMeterReadingRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;

public class WaterMeterReadingServiceImpl implements MeterReadingService<ReadWaterMeterReadingDto, CreateUpdateWaterMeterReadingDto> {

    private final MeterReadingRepository<WaterMeterReading> waterMeterReadingRepositoryImpl = WaterMeterReadingRepositoryImpl.getInstance();
    private final UserRepository userRepositoryImpl = UserRepositoryImpl.getInstance();
    private final WaterMeterMapper waterMeterMapperImpl = WaterMeterMapperImpl.getInstance();
    private static final WaterMeterReadingServiceImpl INSTANCE = new WaterMeterReadingServiceImpl();

    private WaterMeterReadingServiceImpl() {
    }

    public static WaterMeterReadingServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public ReadWaterMeterReadingDto save(CreateUpdateWaterMeterReadingDto object) {
        var userId = object.userId();
        var maybeUser = userRepositoryImpl.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserNotFoundException(""));

        var waterMeterReadingToSave = waterMeterMapperImpl.toWaterMeterReading(
                object,
                maybeUser
        );
        waterMeterReadingToSave.setDateOfMeterReading(LocalDateTime.now());
        return waterMeterMapperImpl.toReadWaterMeterReadingDto(
                waterMeterReadingRepositoryImpl.save(waterMeterReadingToSave
                ));
    }

    @Override
    public ReadWaterMeterReadingDto findActual() {
        var maybeWaterMeter = waterMeterReadingRepositoryImpl.findActual().orElseThrow(
                () -> new RuntimeException("")
        );
        return waterMeterMapperImpl.toReadWaterMeterReadingDto(
                maybeWaterMeter
        );
    }

    @Override
    public List<ReadWaterMeterReadingDto> findAll() {
        return null;
    }

    @Override
    public ReadWaterMeterReadingDto findByFilter(MonthFilter monthFilter) {
        return null;
    }


}
