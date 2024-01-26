package ru.rstdv.monitoringservice.repository;

import ru.rstdv.monitoringservice.dto.filter.MonthFilter;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;

import java.util.List;
import java.util.Optional;

public interface ThermalMeterRepository {

    public ThermalMeterReading save(ThermalMeterReading thermalMeterReading);

    public Optional<ThermalMeterReading> findActual();

    public List<ThermalMeterReading> findAll();

    public Optional<ThermalMeterReading> findByFilter(MonthFilter monthFilter);
}
