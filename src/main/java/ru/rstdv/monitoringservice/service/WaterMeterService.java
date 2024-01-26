package ru.rstdv.monitoringservice.service;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.WaterMeterMapper;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.repository.UserRepositoryImpl;
import ru.rstdv.monitoringservice.repository.WaterMeterRepositoryImpl;

import java.time.LocalDateTime;

public class WaterMeterService {

    private final WaterMeterRepositoryImpl waterMeterRepositoryImpl = WaterMeterRepositoryImpl.getInstance();
    private final UserRepository userRepositoryImpl = UserRepositoryImpl.getInstance();
    private final WaterMeterMapper waterMeterMapper = WaterMeterMapper.getInstance();
    private static final WaterMeterService INSTANCE = new WaterMeterService();

    private WaterMeterService() {
    }

    public static WaterMeterService getInstance() {
        return INSTANCE;
    }

    public ReadWaterMeterReadingDto create(CreateUpdateWaterMeterReadingDto createUpdateWaterMeterReadingDto) {
        var userId = createUpdateWaterMeterReadingDto.userId();
        var maybeUser = userRepositoryImpl.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserNotFoundException(""));

        var waterMeterReadingToSave = waterMeterMapper.toWaterMeterReading(
                createUpdateWaterMeterReadingDto,
                maybeUser
        );
        waterMeterReadingToSave.setDateOfMeterReading(LocalDateTime.now());
        return waterMeterMapper.toReadWaterMeterReadingDto(
                waterMeterRepositoryImpl.save(waterMeterReadingToSave
                ));
    }

    public ReadWaterMeterReadingDto getActual() {
        var maybeWaterMeter = waterMeterRepositoryImpl.getActual().orElseThrow(
                () -> new RuntimeException("")
        );
        return waterMeterMapper.toReadWaterMeterReadingDto(
                maybeWaterMeter
        );
    }

//    public ReadWaterMeterReadingDto getAll(MonthFilter monthFilter) {
//
//    }
}
