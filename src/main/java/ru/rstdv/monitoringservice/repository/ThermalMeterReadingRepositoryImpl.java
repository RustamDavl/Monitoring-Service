package ru.rstdv.monitoringservice.repository;

import ru.rstdv.monitoringservice.dto.filter.Filter;
import ru.rstdv.monitoringservice.entity.MeterReading;
import ru.rstdv.monitoringservice.util.DataBaseTable;

import ru.rstdv.monitoringservice.entity.ThermalMeterReading;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
    public Optional<ThermalMeterReading> findActualByUserId(Long id) {
        var actualThermalMeterReadings = THERMAL_METER_TABLE.GET_ALL()
                .stream()
                .filter(thermalMeterReading -> Objects.equals(thermalMeterReading.getUser().getId(), id))
                .sorted(Comparator.comparing(MeterReading::getDateOfMeterReading, Comparator.reverseOrder()))
                .toList();
        if (actualThermalMeterReadings.isEmpty())
            return Optional.empty();

        return Optional.ofNullable(actualThermalMeterReadings.get(0));
    }

    @Override
    public List<ThermalMeterReading> findAllByUserId(Long id) {
        return THERMAL_METER_TABLE.GET_ALL()
                .stream()
                .filter(thermalMeterReading -> Objects.equals(thermalMeterReading.getUser().getId(), id))
                .toList();
    }

    @Override
    public Optional<ThermalMeterReading> findByMonthAndUserId(Filter filter, Long id) {
        var list = THERMAL_METER_TABLE.GET_ALL().stream()
                .filter(thermalMeterReading -> Objects.equals(thermalMeterReading.getUser().getId(), id))
                .filter(thermalMeterReading -> thermalMeterReading.getDateOfMeterReading().getMonthValue() == filter.getMonthNumber())
                .toList();
        if (list.isEmpty())
            return Optional.empty();

        return Optional.ofNullable(list.get(0));
    }
}
