package ru.rstdv.monitoringservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rstdv.monitoringservice.aspect.annotation.Auditable;
import ru.rstdv.monitoringservice.aspect.annotation.Loggable;
import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilter;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.exception.IncorrectMonthValueException;
import ru.rstdv.monitoringservice.exception.MeterReadingNotFoundException;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.ThermalMeterMapper;
import ru.rstdv.monitoringservice.repository.MeterReadingRepository;
import ru.rstdv.monitoringservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Loggable
@RequiredArgsConstructor
@Service
public class ThermalMeterReadingServiceImpl implements MeterReadingService<ReadThermalMeterReadingDto, CreateUpdateThermalMeterReadingDto> {

    private final MeterReadingRepository<ThermalMeterReading> thermalMeterReadingRepository;
    private final UserRepository userRepository;
    private final ThermalMeterMapper thermalMeterMapper;
    private final AuditService auditService;


    @Auditable
    @Override
    public ReadThermalMeterReadingDto save(CreateUpdateThermalMeterReadingDto object) {
        var userId = object.userId();
        var maybeUser = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserNotFoundException("there is no user with id " + userId));

        var thermalMeterReadingToSave = thermalMeterMapper.toThermalMeterReading(object);
        var savedThermalMeterReading = thermalMeterReadingRepository.save(thermalMeterReadingToSave);

        return thermalMeterMapper.toReadThermalMeterReadingDto(
                savedThermalMeterReading);
    }

    @Auditable
    @Override
    public ReadThermalMeterReadingDto findActualByUserId(Long id) {
        var maybeThermalMeter = thermalMeterReadingRepository.findActualByUserId(id)
                .orElseThrow(() -> new UserNotFoundException("there is no user with id " + id));
        return thermalMeterMapper.toReadThermalMeterReadingDto(maybeThermalMeter);
    }

    @Auditable
    @Override
    public List<ReadThermalMeterReadingDto> findAllByUserId(Long id) {
        var list = thermalMeterReadingRepository.findAllByUserId(id)
                .stream()
                .map(thermalMeterMapper::toReadThermalMeterReadingDto)
                .toList();
        return list;
    }

    @Auditable
    @Override
    public ReadThermalMeterReadingDto findByMonthAndUserId(MonthFilter monthFilter, Long id) {
        if (!isMonthValueCorrect(monthFilter.getMonthNumber()))
            throw new IncorrectMonthValueException("month value must be between [1, 12] but your value is : " + monthFilter.getMonthNumber());
        var readThermalMeterReadingDto = thermalMeterReadingRepository.findByMonthAndUserId(monthFilter, id)
                .map(thermalMeterMapper::toReadThermalMeterReadingDto)
                .orElseThrow(() -> new MeterReadingNotFoundException("there is no any meter reading in " + Month.of(monthFilter.getMonthNumber()).name()));
        return readThermalMeterReadingDto;
    }

    @Auditable
    @Override
    public List<ReadThermalMeterReadingDto> findAll() {
        return thermalMeterReadingRepository.findAll()
                .stream()
                .map(thermalMeterMapper::toReadThermalMeterReadingDto)
                .toList();
    }

    private boolean isMonthValueCorrect(int value) {
        return value >= 1 && value <= 12;
    }
}
