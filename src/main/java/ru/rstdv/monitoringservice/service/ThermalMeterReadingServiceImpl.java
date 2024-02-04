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

    private final MeterReadingRepository<ThermalMeterReading> thermalMeterReadingRepositoryImpl;
    private final UserRepository userRepositoryImpl;
    private final ThermalMeterMapper thermalMeterMapperImpl;
    private final AuditService auditServiceImpl;


    @Override
    public ReadThermalMeterReadingDto save(CreateUpdateThermalMeterReadingDto object) {
        var userId = object.userId();
        var maybeUser = userRepositoryImpl.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserNotFoundException("there is no user with id " + userId));

        var thermalMeterReadingToSave = thermalMeterMapperImpl.toThermalMeterReading(
                object,
                maybeUser
        );
        var savedThermalMeterReading = thermalMeterReadingRepositoryImpl.save(thermalMeterReadingToSave);

//        auditServiceImpl.saveAudit(new CreateAuditDto(
//                object.userId(),
//                AuditAction.THERMAL_METER_READING_SENDING.name()
//               // thermalMeterReadingToSave.getDateOfMeterReading(),
//
//        ));


        return thermalMeterMapperImpl.toReadThermalMeterReadingDto(
                savedThermalMeterReading);
    }

    @Override
    public ReadThermalMeterReadingDto findActualByUserId(Long id) {
        var maybeThermalMeter = thermalMeterReadingRepositoryImpl.findActualByUserId(id)
                .orElseThrow(() -> new UserNotFoundException("there is no user with id " + id));

        auditServiceImpl.saveAudit(new CreateAuditDto(
                id.toString(),
                AuditAction.GET_ACTUAL_THERMAL_METER_READING.name(),
                LocalDateTime.now(),
                "get actual result"
        ));
        return thermalMeterMapperImpl.toReadThermalMeterReadingDto(maybeThermalMeter);
    }

    @Override
    public List<ReadThermalMeterReadingDto> findAllByUserId(Long id) {
        var list = thermalMeterReadingRepositoryImpl.findAllByUserId(id)
                .stream()
                .map(thermalMeterMapperImpl::toReadThermalMeterReadingDto)
                .toList();
        auditServiceImpl.saveAudit(
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
        var readThermalMeterReadingDto = thermalMeterReadingRepositoryImpl.findByMonthAndUserId(monthFilter, id)
                .map(thermalMeterMapperImpl::toReadThermalMeterReadingDto)
                .orElseThrow(() -> new MeterReadingNotFoundException("there is no any meter reading in " + Month.of(monthFilter.getMonthNumber()).name()));

        auditServiceImpl.saveAudit(
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
        return thermalMeterReadingRepositoryImpl.findAll()
                .stream()
                .map(thermalMeterMapperImpl::toReadThermalMeterReadingDto)
                .toList();
    }

    private boolean isMonthValueCorrect(int value) {
        return value >= 1 && value <= 12;
    }
}
