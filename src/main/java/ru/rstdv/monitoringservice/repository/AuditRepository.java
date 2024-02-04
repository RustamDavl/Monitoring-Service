package ru.rstdv.monitoringservice.repository;

import ru.rstdv.monitoringservice.entity.Audit;


import java.util.List;

/**
 * интерфейс AuditRepository предназначен для аудита действий пользователя
 * @author RustamD
 * @version 1.0
 */
public interface AuditRepository {

    /**
     * @param userId - идентификатор пользователя
     * @return - возвращает список типа Audit, который содержит информацию о пользователе и его действие
     */

    List<Audit> findByUserId(Long userId);

    /**
     * метод сохраняет объект типа Audit
     *
     * @param audit - объект аудита
     */
    void save(Audit audit);

}
