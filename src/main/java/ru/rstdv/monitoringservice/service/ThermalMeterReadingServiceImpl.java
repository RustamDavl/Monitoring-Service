package ru.rstdv.monitoringservice.service;

import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
public class ThermalMeterReadingServiceImpl implements MeterReadingService<ReadThermalMeterReadingDto, CreateUpdateThermalMeterReadingDto> {

    private final MeterReadingRepository<ThermalMeterReading> thermalMeterReadingRepository;
    private final UserRepository userRepository;
    private final ThermalMeterMapper thermalMeterMapper;
    private final AuditService auditService;


    @Override
    public ReadThermalMeterReadingDto save(CreateUpdateThermalMeterReadingDto object) {
        var userId = object.userId();
        var maybeUser = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserNotFoundException("there is no user with id " + userId));

        var thermalMeterReadingToSave = thermalMeterMapper.toThermalMeterReading(object);
        var savedThermalMeterReading = thermalMeterReadingRepository.save(thermalMeterReadingToSave);

        auditService.saveAudit(new CreateAuditDto(
                userId,
                AuditAction.THERMAL_METER_READING_SENDING.name(),
                LocalDateTime.now(),
                "thermal meter reading saved"
        ));


        return thermalMeterMapper.toReadThermalMeterReadingDto(
                savedThermalMeterReading);
    }

    @Override
    public ReadThermalMeterReadingDto findActualByUserId(Long id) {
        var maybeThermalMeter = thermalMeterReadingRepository.findActualByUserId(id)
                .orElseThrow(() -> new UserNotFoundException("there is no user with id " + id));

        auditService.saveAudit(new CreateAuditDto(
                id.toString(),
                AuditAction.GET_ACTUAL_THERMAL_METER_READING.name(),
                LocalDateTime.now(),
                "get actual result"
        ));
        return thermalMeterMapper.toReadThermalMeterReadingDto(maybeThermalMeter);
    }

    @Override
    public List<ReadThermalMeterReadingDto> findAllByUserId(Long id) {
        var list = thermalMeterReadingRepository.findAllByUserId(id)
                .stream()
                .map(thermalMeterMapper::toReadThermalMeterReadingDto)
                .toList();
        auditService.saveAudit(
                new CreateAuditDto(
                        id.toString(),
                        AuditAction.GET_THERMAL_READING_HISTORY.name(),
                        LocalDateTime.now(),
                        "user got history of thermal meter reading"
                )
        );
        return list;
    }

    @Override
    public ReadThermalMeterReadingDto findByMonthAndUserId(MonthFilter monthFilter, Long id) {
        if (!isMonthValueCorrect(monthFilter.getMonthNumber()))
            throw new IncorrectMonthValueException("month value must be between [1, 12] but your value is : " + monthFilter.getMonthNumber());
        var readThermalMeterReadingDto = thermalMeterReadingRepository.findByMonthAndUserId(monthFilter, id)
                .map(thermalMeterMapper::toReadThermalMeterReadingDto)
                .orElseThrow(() -> new MeterReadingNotFoundException("there is no any meter reading in " + Month.of(monthFilter.getMonthNumber()).name()));

        auditService.saveAudit(
                new CreateAuditDto(
                        id.toString(),
                        AuditAction.GET_THERMAL_READING_BY_MONTH.name(),
                        LocalDateTime.now(),
                        "user got thermal meter reading by month : " + Month.of(monthFilter.getMonthNumber())
                )
        );
        return readThermalMeterReadingDto;

    }

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
