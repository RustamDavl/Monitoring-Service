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

    private final MeterReadingRepository<WaterMeterReading> waterMeterReadingRepository;
    private final UserRepository userRepository;
    private final WaterMeterMapper waterMeterMapper;
    private final AuditService auditService;


    @Override
    public ReadWaterMeterReadingDto save(CreateUpdateWaterMeterReadingDto object) {
        var userId = object.userId();
        var maybeUser = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserNotFoundException(""));

        var waterMeterReadingToSave = waterMeterMapper.toWaterMeterReading(
                object
        );
        var savedWaterMeterReading = waterMeterReadingRepository.save(waterMeterReadingToSave);

        auditService.saveAudit(new CreateAuditDto(
                object.userId(),
                AuditAction.WATER_METER_READING_SENDING.name(),
                LocalDateTime.now(),
                "water meter readings was saved"
        ));
        return waterMeterMapper.toReadWaterMeterReadingDto(savedWaterMeterReading);
    }

    @Override
    public ReadWaterMeterReadingDto findActualByUserId(Long id) {
        var maybeWaterMeter = waterMeterReadingRepository.findActualByUserId(id)
                .orElseThrow(() -> new UserNotFoundException("there is no user with id " + id));

        auditService.saveAudit(new CreateAuditDto(
                id.toString(),
                AuditAction.GET_ACTUAL_WATER_METER_READING.name(),
                LocalDateTime.now(),
                "get actual result"
        ));
        return waterMeterMapper.toReadWaterMeterReadingDto(
                maybeWaterMeter
        );
    }

    @Override
    public List<ReadWaterMeterReadingDto> findAllByUserId(Long id) {
        var list = waterMeterReadingRepository.findAllByUserId(id)
                .stream()
                .map(waterMeterMapper::toReadWaterMeterReadingDto)
                .toList();

        auditService.saveAudit(new CreateAuditDto(
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

        var list = waterMeterReadingRepository.findByMonthAndUserId(monthFilter, id)
                .map(waterMeterMapper::toReadWaterMeterReadingDto)
                .orElseThrow(() -> new MeterReadingNotFoundException("there is no any meter reading in " + Month.of(monthFilter.getMonthNumber()).name()));

        auditService.saveAudit(
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
        return waterMeterReadingRepository.findAll()
                .stream()
                .map(waterMeterMapper::toReadWaterMeterReadingDto)
                .toList();
    }

    private boolean isMonthValueCorrect(int value) {
        return value >= 1 && value <= 12;
    }


}
