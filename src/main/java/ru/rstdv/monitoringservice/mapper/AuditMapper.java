package ru.rstdv.monitoringservice.mapper;

import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.read.ReadAuditDto;
import ru.rstdv.monitoringservice.entity.Audit;
import ru.rstdv.monitoringservice.entity.User;

public interface AuditMapper {

    ReadAuditDto toReadAuditDto(Audit audit);

    Audit toAudit(CreateAuditDto createAuditDto, User user);

}
