package ru.rstdv.monitoringservice.repository;

import ru.rstdv.monitoringservice.util.DataBaseTable;

import ru.rstdv.monitoringservice.dto.filter.MonthFilter;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;

import java.util.List;
import java.util.Optional;

public class ThermalMeterReadingRepositoryImpl implements MeterReadingRepository<ThermalMeterReading> {
    private static final ThermalMeterReadingRepositoryImpl INSTANCE = new ThermalMeterReadingRepositoryImpl();
    private static final DataBaseTable<ThermalMeterReading> THERMAL_METER_TABLE = new DataBaseTable<>();

    private ThermalMeterReadingRepositoryImpl() {
    }

    public static ThermalMeterReadingRepositoryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public ThermalMeterReading save(ThermalMeterReading thermalMeterReading) {
        return THERMAL_METER_TABLE.INSERT(thermalMeterReading);
    }

    @Override
    public Optional<ThermalMeterReading> findActual() {
        return Optional.ofNullable(THERMAL_METER_TABLE.GET_LAST());
    }

    @Override
    public List<ThermalMeterReading> findAll() {
        return THERMAL_METER_TABLE.GET_ALL();
    }

    @Override
    public Optional<ThermalMeterReading> findByFilter(MonthFilter monthFilter) {
        var list = THERMAL_METER_TABLE.GET_ALL().stream()
                .filter(thermalMeterReading -> thermalMeterReading.getDateOfMeterReading().getMonthValue() == monthFilter.getMonthValue())
                .toList();
        if (list.isEmpty())
            return Optional.empty();

        return Optional.ofNullable(list.get(0));
    }
}
