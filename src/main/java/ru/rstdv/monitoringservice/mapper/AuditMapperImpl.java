package ru.rstdv.monitoringservice.mapper;

import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.read.ReadAuditDto;
import ru.rstdv.monitoringservice.entity.Audit;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;


public class AuditMapperImpl implements AuditMapper {

    @Override
    public ReadAuditDto toReadAuditDto(Audit audit) {
        return new ReadAuditDto(
                audit.getId().toString(),
                audit.getUserId().toString(),
                audit.getAuditDateTime().toString(),
                audit.getAuditAction().name(),
                audit.getDescription()
        );
    }

    @Override
    public Audit toAudit(CreateAuditDto createAuditDto, User user) {
        return Audit.builder()
                .userId(user.getId())
                .description(createAuditDto.description())
                .auditAction(AuditAction.valueOf(createAuditDto.auditAction()))
                .auditDateTime(createAuditDto.auditDateTime())
                .build();
    }
}
