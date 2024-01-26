package ru.rstdv.monitoringservice.repository;

import ru.rstdv.monitoringservice.dto.filter.MonthFilter;
import ru.rstdv.monitoringservice.entity.MeterReading;

import java.util.List;
import java.util.Optional;

public interface MeterReadingRepository <T extends MeterReading> {

    T save(T thermalMeterReading);

    Optional<T> findActual();

    List<T> findAll();

    Optional<T> findByFilter(MonthFilter monthFilter);
}
