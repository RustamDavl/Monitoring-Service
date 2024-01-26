package ru.rstdv.monitoringservice.repository;

import ru.rstdv.monitoringservice.dto.filter.MonthFilter;
import ru.rstdv.monitoringservice.util.DataBaseTable;

import ru.rstdv.monitoringservice.entity.WaterMeterReading;

import java.util.List;
import java.util.Optional;

public class WaterMeterReadingRepositoryImpl implements MeterReadingRepository<WaterMeterReading> {
    private static final WaterMeterReadingRepositoryImpl INSTANCE = new WaterMeterReadingRepositoryImpl();
    private static final DataBaseTable<WaterMeterReading> WATER_METER_TABLE = new DataBaseTable<>();

    private WaterMeterReadingRepositoryImpl() {
    }

    public static WaterMeterReadingRepositoryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public WaterMeterReading save(WaterMeterReading waterMeterReading) {
        return WATER_METER_TABLE.INSERT(waterMeterReading);
    }

    @Override
    public Optional<WaterMeterReading> findActual() {
        return Optional.ofNullable(WATER_METER_TABLE.GET_LAST());
    }

    @Override
    public List<WaterMeterReading> findAll() {
        return null;
    }

    @Override
    public Optional<WaterMeterReading> findByFilter(MonthFilter monthFilter) {
        return Optional.empty();
    }

}
