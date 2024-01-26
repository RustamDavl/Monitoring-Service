package ru.rstdv.monitoringservice.repository;

import ru.rstdv.monitoringservice.util.DataBaseTable;

import ru.rstdv.monitoringservice.entity.WaterMeterReading;

import java.util.Optional;

public class WaterMeterRepositoryImpl {
    private static final WaterMeterRepositoryImpl INSTANCE = new WaterMeterRepositoryImpl();
    private static final DataBaseTable<WaterMeterReading> WATER_METER_TABLE = new DataBaseTable<>();

    private WaterMeterRepositoryImpl() {
    }

    public static WaterMeterRepositoryImpl getInstance() {
        return INSTANCE;
    }

    public WaterMeterReading save(WaterMeterReading waterMeterReading) {
        return WATER_METER_TABLE.INSERT(waterMeterReading);
    }

    public Optional<WaterMeterReading> getActual() {
        return Optional.ofNullable(WATER_METER_TABLE.GET_LAST());
    }
}
