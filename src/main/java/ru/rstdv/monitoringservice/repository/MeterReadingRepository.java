package ru.rstdv.monitoringservice.repository;

import ru.rstdv.monitoringservice.dto.filter.MonthFilter;
import ru.rstdv.monitoringservice.entity.MeterReading;

import java.util.List;
import java.util.Optional;

/**
 * интерфейс MeterReadingRepository предоставляет операции для взаимодействия с базой данных для объекта типа MeterReading
 *
 * @param <T> - параметр типа MeterReading
 * @author RustamD
 * @version 1.0
 */
public interface MeterReadingRepository<T extends MeterReading> {

    /**
     * сохраняет показание счетчика
     *
     * @param thermalMeterReading - сохраняемое показание
     * @return - сохраненное показание с присвоенным идентификатором
     */
    T save(T thermalMeterReading);

    /**
     * производит поиск актуального показания пользователя
     *
     * @param id - идентификатор пользователя
     * @return - Optional.empty() если показание не найдено, иначе Optional.of(meterReading)
     */
    Optional<T> findActualByUserId(Long id);

    /**
     * производит поиск всех показаний пользователя
     *
     * @param id - идентификатор пользователя
     * @return - список всех показаний пользователя
     */
    List<T> findAllByUserId(Long id);

    /**
     * производит поиск показания пользователя на определенный месяц
     *
     * @param monthFilter - фильтр
     * @param id     - идентификатор пользователя
     * @return - Optional.empty() если показание не найдено, иначе Optional.of(meterReading)
     */
    Optional<T> findByMonthAndUserId(MonthFilter monthFilter, Long id);

    /**
     * производит поиск всех показаний
     *
     * @return - список всех показаний
     */
    List<T> findAll();
}
