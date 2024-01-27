package ru.rstdv.monitoringservice.service;

import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.Filter;
import ru.rstdv.monitoringservice.dto.filter.MonthFilter;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.exception.MeterReadingNotFound;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.WaterMeterMapper;
import ru.rstdv.monitoringservice.mapper.WaterMeterMapperImpl;
import ru.rstdv.monitoringservice.repository.MeterReadingRepository;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.repository.UserRepositoryImpl;
import ru.rstdv.monitoringservice.repository.WaterMeterReadingRepositoryImpl;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

public class WaterMeterReadingServiceImpl implements MeterReadingService<ReadWaterMeterReadingDto, CreateUpdateWaterMeterReadingDto> {

    private final MeterReadingRepository<WaterMeterReading> waterMeterReadingRepositoryImpl = WaterMeterReadingRepositoryImpl.getInstance();
    private final UserRepository userRepositoryImpl = UserRepositoryImpl.getInstance();
    private final WaterMeterMapper waterMeterMapperImpl = WaterMeterMapperImpl.getInstance();

    private final AuditService auditServiceImpl = AuditServiceImpl.getInstance();
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
        var savedWaterMeterReading = waterMeterReadingRepositoryImpl.save(waterMeterReadingToSave);

        auditServiceImpl.saveAudit(new CreateAuditDto(
                object.userId(),
                AuditAction.WATER_METER_READING_SENDING.name(),
                LocalDateTime.now(),
                "water meter readings saved"
        ));

        return waterMeterMapperImpl.toReadWaterMeterReadingDto(savedWaterMeterReading);
    }

    @Override
    public ReadWaterMeterReadingDto findActualByUserId(Long id) {
        var maybeWaterMeter = waterMeterReadingRepositoryImpl.findActualByUserId(id).orElseThrow(
                () -> new RuntimeException("")
        );
        auditServiceImpl.saveAudit(new CreateAuditDto(
                id.toString(),
                AuditAction.GET_ACTUAL_WATER_METER_READING.name(),
                LocalDateTime.now(),
                "get actual result"
        ));
        return waterMeterMapperImpl.toReadWaterMeterReadingDto(
                maybeWaterMeter
        );
    }

    @Override
    public List<ReadWaterMeterReadingDto> findAllByUserId(Long id) {
        var list = waterMeterReadingRepositoryImpl.findAllByUserId(id)
                .stream()
                .map(waterMeterMapperImpl::toReadWaterMeterReadingDto)
                .toList();
        auditServiceImpl.saveAudit(new CreateAuditDto(
                id.toString(),
                AuditAction.WATER_METER_READING_SENDING.name(),
                LocalDateTime.now(),
                "get all water meter readings of user " + id
        ));
        return list;
    }

    @Override
    public ReadWaterMeterReadingDto findByMonthAndUserId(Filter filter, Long id) {
        return waterMeterReadingRepositoryImpl.findByMonthAndUserId(filter, id)
                .map(waterMeterMapperImpl::toReadWaterMeterReadingDto)
                .orElseThrow(() -> new MeterReadingNotFound("there is no any meter reading in " + Month.of(filter.getMonthNumber()).name()));
    }


}
