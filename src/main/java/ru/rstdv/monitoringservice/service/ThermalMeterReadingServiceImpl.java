package ru.rstdv.monitoringservice.service;

import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.Filter;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.exception.MeterReadingNotFound;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.ThermalMeterMapper;
import ru.rstdv.monitoringservice.mapper.ThermalMeterMapperImpl;
import ru.rstdv.monitoringservice.repository.MeterReadingRepository;
import ru.rstdv.monitoringservice.repository.ThermalMeterReadingRepositoryImpl;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.repository.UserRepositoryImpl;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class ThermalMeterReadingServiceImpl implements MeterReadingService<ReadThermalMeterReadingDto, CreateUpdateThermalMeterReadingDto> {

    private final MeterReadingRepository<ThermalMeterReading> thermalMeterReadingRepositoryImpl = ThermalMeterReadingRepositoryImpl.getInstance();
    private final UserRepository userRepositoryImpl = UserRepositoryImpl.getInstance();
    private final ThermalMeterMapper thermalMeterMapperImpl = ThermalMeterMapperImpl.getInstance();

    private final AuditService auditServiceImpl = AuditServiceImpl.getInstance();
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

        var thermalMeterReadingToSave = thermalMeterMapperImpl.toThermalMeterReading(
                object,
                maybeUser
        );
        thermalMeterReadingToSave.setDateOfMeterReading(LocalDateTime.now());

        var savedThermalMeterReading = thermalMeterReadingRepositoryImpl.save(thermalMeterReadingToSave);

        auditServiceImpl.saveAudit(new CreateAuditDto(
                object.userId(),
                AuditAction.THERMAL_METER_READING_SENDING.name(),
                LocalDateTime.now(),
                "thermal meter readings saved"
        ));


        return thermalMeterMapperImpl.toReadThermalMeterReadingDto(
                savedThermalMeterReading);
    }

    @Override
    public ReadThermalMeterReadingDto findActualByUserId(Long id) {
        var maybeThermalMeter = thermalMeterReadingRepositoryImpl.findActualByUserId(id)
                .orElseThrow(() -> new RuntimeException(""));

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
        return thermalMeterReadingRepositoryImpl.findAllByUserId(id)
                .stream()
                .map(thermalMeterMapperImpl::toReadThermalMeterReadingDto)
                .toList();
    }

    @Override
    public ReadThermalMeterReadingDto findByMonthAndUserId(Filter filter, Long id) {
        return thermalMeterReadingRepositoryImpl.findByMonthAndUserId(filter, id)
                .map(thermalMeterMapperImpl::toReadThermalMeterReadingDto)
                .orElseThrow(() -> new MeterReadingNotFound("there is no any meter reading in " + Month.of(filter.getMonthNumber()).name()));

    }
}
