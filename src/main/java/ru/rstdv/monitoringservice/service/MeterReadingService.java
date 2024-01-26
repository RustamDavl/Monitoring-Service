package ru.rstdv.monitoringservice.service;


import ru.rstdv.monitoringservice.dto.filter.MonthFilter;

import java.util.List;

public interface MeterReadingService <R, C> {
    public R save(C object);

    public R findActual();

    public List<R> findAll();

    public R findByFilter(MonthFilter monthFilter);
}
