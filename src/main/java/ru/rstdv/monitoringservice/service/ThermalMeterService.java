package ru.rstdv.monitoringservice.service;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilter;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.ThermalMeterMapper;
import ru.rstdv.monitoringservice.repository.ThermalMeterRepositoryImpl;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.repository.UserRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;

public class ThermalMeterService {

    private  final ThermalMeterRepositoryImpl thermalMeterRepositoryImpl = ThermalMeterRepositoryImpl.getInstance();
    private  final UserRepository userRepositoryImpl = UserRepositoryImpl.getInstance();
    private  final ThermalMeterMapper thermalMeterMapper = ThermalMeterMapper.getInstance();
    private static final ThermalMeterService INSTANCE = new ThermalMeterService();

    private ThermalMeterService() {
    }

    public static ThermalMeterService getInstance() {
        return INSTANCE;
    }

    public ReadThermalMeterReadingDto create(CreateUpdateThermalMeterReadingDto createUpdateThermalMeterReadingDto) {
        var userId = createUpdateThermalMeterReadingDto.userId();
        var maybeUser = userRepositoryImpl.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserNotFoundException(""));

        var thermalMeterReadingToSave = thermalMeterRepositoryImpl.save(thermalMeterMapper.toThermalMeterReading(
                createUpdateThermalMeterReadingDto,
                maybeUser
        ));
        thermalMeterReadingToSave.setDateOfMeterReading(LocalDateTime.now());

        return thermalMeterMapper.toReadWaterMeterReadingDto(
                thermalMeterRepositoryImpl.save(thermalMeterReadingToSave));
    }

    public ReadThermalMeterReadingDto getActual() {
        var maybeThermalMeter = thermalMeterRepositoryImpl.findActual()
                .orElseThrow(() -> new RuntimeException(""));
        return thermalMeterMapper.toReadWaterMeterReadingDto(maybeThermalMeter);
    }

    public List<ReadThermalMeterReadingDto> getAll() {

        return thermalMeterRepositoryImpl.findAll()
                .stream()
                .map(thermalMeterMapper::toReadWaterMeterReadingDto)
                .toList();
    }

    public ReadThermalMeterReadingDto getByFilter(MonthFilter monthFilter) {
        return thermalMeterRepositoryImpl.findByFilter(monthFilter)
                .map(thermalMeterMapper::toReadWaterMeterReadingDto)
                .orElse(null);
    }
}
