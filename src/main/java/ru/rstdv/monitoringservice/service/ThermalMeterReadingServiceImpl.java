package ru.rstdv.monitoringservice.service;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilter;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.ThermalMeterMapper;
import ru.rstdv.monitoringservice.mapper.ThermalMeterMapperImpl;
import ru.rstdv.monitoringservice.repository.MeterReadingRepository;
import ru.rstdv.monitoringservice.repository.ThermalMeterReadingRepositoryImpl;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.repository.UserRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;

public class ThermalMeterReadingServiceImpl implements MeterReadingService<ReadThermalMeterReadingDto, CreateUpdateThermalMeterReadingDto> {

    private final MeterReadingRepository<ThermalMeterReading> thermalMeterReadingRepositoryImpl = ThermalMeterReadingRepositoryImpl.getInstance();
    private final UserRepository userRepositoryImpl = UserRepositoryImpl.getInstance();
    private final ThermalMeterMapper thermalMeterMapperImpl = ThermalMeterMapperImpl.getInstance();
    private static final ThermalMeterReadingServiceImpl INSTANCE = new ThermalMeterReadingServiceImpl();

    private ThermalMeterReadingServiceImpl() {
    }

    public static ThermalMeterReadingServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public ReadThermalMeterReadingDto save(CreateUpdateThermalMeterReadingDto object) {
        var userId = object.userId();
        var maybeUser = userRepositoryImpl.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserNotFoundException(""));

        var thermalMeterReadingToSave = thermalMeterReadingRepositoryImpl.save(thermalMeterMapperImpl.toThermalMeterReading(
                object,
                maybeUser
        ));
        thermalMeterReadingToSave.setDateOfMeterReading(LocalDateTime.now());

        return thermalMeterMapperImpl.toReadWaterMeterReadingDto(
                thermalMeterReadingToSave);
    }

    @Override
    public ReadThermalMeterReadingDto findActual() {
        var maybeThermalMeter = thermalMeterReadingRepositoryImpl.findActual()
                .orElseThrow(() -> new RuntimeException(""));
        return thermalMeterMapperImpl.toReadWaterMeterReadingDto(maybeThermalMeter);
    }

    @Override
    public List<ReadThermalMeterReadingDto> findAll() {
        return thermalMeterReadingRepositoryImpl.findAll()
                .stream()
                .map(thermalMeterMapperImpl::toReadWaterMeterReadingDto)
                .toList();
    }

    @Override
    public ReadThermalMeterReadingDto findByFilter(MonthFilter monthFilter) {
        return thermalMeterReadingRepositoryImpl.findByFilter(monthFilter)
                .map(thermalMeterMapperImpl::toReadWaterMeterReadingDto)
                .orElse(null);

    }
}
