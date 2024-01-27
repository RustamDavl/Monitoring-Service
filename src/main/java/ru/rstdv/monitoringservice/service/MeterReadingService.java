package ru.rstdv.monitoringservice.service;


import ru.rstdv.monitoringservice.dto.filter.Filter;
import ru.rstdv.monitoringservice.dto.filter.MonthFilter;

import java.util.List;

public interface MeterReadingService<R, C> {
    R save(C object);

    R findActualByUserId(Long id);

    List<R> findAllByUserId(Long id);

    R findByMonthAndUserId(Filter filter, Long id);
}
