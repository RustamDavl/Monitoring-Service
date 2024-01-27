package ru.rstdv.monitoringservice.repository;

import ru.rstdv.monitoringservice.dto.filter.Filter;
import ru.rstdv.monitoringservice.dto.filter.MonthFilter;
import ru.rstdv.monitoringservice.entity.MeterReading;
import ru.rstdv.monitoringservice.util.DataBaseTable;

import ru.rstdv.monitoringservice.entity.WaterMeterReading;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
    public Optional<WaterMeterReading> findActualByUserId(Long id) {
        var actualWaterMeterReadings = WATER_METER_TABLE.GET_ALL().stream()
                .filter(thermalMeterReading -> Objects.equals(thermalMeterReading.getUser().getId(), id))
                .sorted(Comparator.comparing(MeterReading::getDateOfMeterReading, Comparator.reverseOrder()))
                .toList();
        if (actualWaterMeterReadings.isEmpty())
            return Optional.empty();

        return Optional.ofNullable(actualWaterMeterReadings.get(0));
    }

    @Override
    public List<WaterMeterReading> findAllByUserId(Long id) {
        return WATER_METER_TABLE.GET_ALL()
                .stream()
                .filter(thermalMeterReading -> Objects.equals(thermalMeterReading.getUser().getId(), id))
                .toList();
    }

    @Override
    public Optional<WaterMeterReading> findByMonthAndUserId(Filter filter, Long id) {
        var list = WATER_METER_TABLE.GET_ALL().stream()
                .filter(waterMeterReading -> Objects.equals(waterMeterReading.getUser().getId(), id))
                .filter(thermalMeterReading -> thermalMeterReading.getDateOfMeterReading().getMonthValue() == filter.getMonthNumber())
                .toList();
        if (list.isEmpty())
            return Optional.empty();

        return Optional.ofNullable(list.get(0));
    }

}
