package ru.rstdv.monitoringservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.read.ReadAuditDto;
import ru.rstdv.monitoringservice.entity.Audit;

/**
 * интерфейс AuditMapper необходим для маппинга сущностей
 *
 * @author RustamD
 * @version 1.0
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
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
     * @return сохраняемый audit
     */
    Audit toAudit(CreateAuditDto createAuditDto);

}
