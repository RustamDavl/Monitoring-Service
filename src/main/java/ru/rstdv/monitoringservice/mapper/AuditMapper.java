package ru.rstdv.monitoringservice.mapper;

import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.read.ReadAuditDto;
import ru.rstdv.monitoringservice.entity.Audit;
import ru.rstdv.monitoringservice.entity.User;

/**
 * интерфейс AuditMapper необходим для маппинга сущностей
 *
 * @author RustamD
 * @version 1.0
 */
public interface AuditMapper {

    /**
     * маппит объект типа Audit в ReadAuditDto, который передается пользователю
     *
     * @param audit аудит
     * @return аудит с полями, разрешенными для чтения
     */
    ReadAuditDto toReadAuditDto(Audit audit);

    /**
     * маппит объект типа CreateAuditDto в Audit, который сохраняется в базу
     *
     * @param createAuditDto созданный аудит
     * @param user           пользователь, аудит которого сохраняется
     * @return сохраняемый audit
     */
    Audit toAudit(CreateAuditDto createAuditDto, User user);

}
