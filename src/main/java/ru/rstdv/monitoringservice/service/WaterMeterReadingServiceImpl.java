package ru.rstdv.monitoringservice.service;

import lombok.RequiredArgsConstructor;
import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilter;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.exception.IncorrectMonthValueException;
import ru.rstdv.monitoringservice.exception.MeterReadingNotFoundException;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.WaterMeterMapper;
import ru.rstdv.monitoringservice.repository.MeterReadingRepository;
import ru.rstdv.monitoringservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@RequiredArgsConstructor
public class WaterMeterReadingServiceImpl implements MeterReadingService<ReadWaterMeterReadingDto, CreateUpdateWaterMeterReadingDto> {

    private final MeterReadingRepository<WaterMeterReading> waterMeterReadingRepositoryImpl;
    private final UserRepository userRepositoryImpl;
    private final WaterMeterMapper waterMeterMapperImpl;
    private final AuditService auditServiceImpl;


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
                AuditAction.GET_WATER_READING_HISTORY.name(),
                LocalDateTime.now(),
                "get all water meter readings of user " + id
        ));
        return list;
    }

    @Override
    public ReadWaterMeterReadingDto findByMonthAndUserId(MonthFilter monthFilter, Long id) {
        if (!isMonthValueCorrect(monthFilter.getMonthNumber()))
            throw new IncorrectMonthValueException("month value must be between [1, 12] but your value is : " + monthFilter.getMonthNumber());

        var list = waterMeterReadingRepositoryImpl.findByMonthAndUserId(monthFilter, id)
                .map(waterMeterMapperImpl::toReadWaterMeterReadingDto)
                .orElseThrow(() -> new MeterReadingNotFoundException("there is no any meter reading in " + Month.of(monthFilter.getMonthNumber()).name()));

        auditServiceImpl.saveAudit(
                new CreateAuditDto(
                        id.toString(),
                        AuditAction.GET_WATER_READING_BY_MONTH.name(),
                        LocalDateTime.now(),
                        "user got water meter reading by month : " + Month.of(monthFilter.getMonthNumber())
                )
        );
        return list;
    }

    @Override
    public List<ReadWaterMeterReadingDto> findAll() {
        return waterMeterReadingRepositoryImpl.findAll()
                .stream()
                .map(waterMeterMapperImpl::toReadWaterMeterReadingDto)
                .toList();
    }

    private boolean isMonthValueCorrect(int value) {
        return value >= 1 && value <= 12;
    }


}
