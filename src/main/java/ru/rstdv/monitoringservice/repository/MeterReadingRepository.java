package ru.rstdv.monitoringservice.repository;

import ru.rstdv.monitoringservice.dto.filter.Filter;
import ru.rstdv.monitoringservice.entity.MeterReading;

import java.util.List;
import java.util.Optional;

public interface MeterReadingRepository <T extends MeterReading> {

    T save(T thermalMeterReading);

    Optional<T> findActualByUserId(Long id);

    List<T> findAllByUserId(Long id);

    Optional<T> findByMonthAndUserId(Filter filter, Long id);
}
