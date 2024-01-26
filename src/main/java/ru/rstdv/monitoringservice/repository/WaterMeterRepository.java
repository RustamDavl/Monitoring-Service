package ru.rstdv.monitoringservice.repository;

import ru.rstdv.monitoringservice.entity.WaterMeterReading;
import ru.rstdv.monitoringservice.util.DataBaseTable;

import java.util.Optional;

public interface WaterMeterRepository {
    WaterMeterReading save(WaterMeterReading waterMeterReading);

    Optional<WaterMeterReading> findActual();
}
