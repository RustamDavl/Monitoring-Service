package ru.rstdv.monitoringservice.service;

import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.read.ReadAuditDto;

import java.util.List;

/**
 * интерфейс AuditService предназначен для аудита действий пользователя, а так же маппинга сущностей
 * @author RustamD
 * @version 1.0
 */
public interface AuditService {

    /**
     * @param userId - идентификатор пользователя
     * @return - возвращает список типа ReadAuditDto, который содержит информацию о пользователе и его действие
     */
    List<ReadAuditDto> findUserAudits(String userId);

    /**
     * метод сохраняет объект типа Audit
     *
     * @param createAuditDto - объект CreateAuditDto
     */
    void saveAudit(CreateAuditDto createAuditDto);
}
