package ru.rstdv.monitoringservice.service;


import ru.rstdv.monitoringservice.dto.filter.MonthFilter;

import java.util.List;

/**
 * интерфейс MeterReadingRepository предоставляет операции для взаимодействия с базой данных для объекта типа MeterReading
 *
 * @param <R> - параметр чтения показания
 * @param <C> - параметр создания показания
 * @author RustamD
 * @version 1.0
 */
public interface MeterReadingService<R, C> {

    /**
     * сохраняет показание счетчика
     *
     * @param object - сохраняемое показание
     * @return - сохраненное показание с присвоенным идентификатором, передоваемое пользователю
     */
    R save(C object);

    /**
     * производит поиск актуального показания пользователя
     *
     * @param id - идентификатор пользователя
     * @return - Optional.empty() если показание не найдено, иначе Optional.of(readMeterReadingDto)
     */
    R findActualByUserId(Long id);

    /**
     * производит поиск всех показаний пользователя
     *
     * @param id - идентификатор пользователя
     * @return - список всех показаний, передаваемое пользователю
     */
    List<R> findAllByUserId(Long id);
    /**
     * производит поиск показания пользователя на определенный месяц
     *
     * @param monthFilter - фильтр
     * @param id     - идентификатор пользователя
     * @return - Optional.empty() если показание не найдено, иначе Optional.of(meterReading)
     */
    R findByMonthAndUserId(MonthFilter monthFilter, Long id);

    /**
     * производит поиск всех показаний
     *
     * @return - список всех показаний, переаваемых пользователю
     */
    List<R> findAll();
}
