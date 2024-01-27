package ru.rstdv.monitoringservice.mapper;

import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.read.ReadAuditDto;
import ru.rstdv.monitoringservice.entity.Audit;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;

import java.time.LocalDateTime;

public class AuditMapperImpl implements AuditMapper {

    private final UserMapper userMapperImpl = UserMapperImpl.getInstance();

    private static final AuditMapperImpl INSTANCE = new AuditMapperImpl();

    private AuditMapperImpl() {
    }

    public static AuditMapperImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public ReadAuditDto toReadAuditDto(Audit audit) {
        return new ReadAuditDto(
                audit.getId().toString(),
                userMapperImpl.toReadUserDto(
                        audit.getUser()
                ),
                audit.getAuditDateTime().toString(),
                audit.getAuditAction().name(),
                audit.getDescription()
        );
    }

    @Override
    public Audit toAudit(CreateAuditDto createAuditDto, User user) {
        return Audit.builder()
                .user(user)
                .description(createAuditDto.description())
                .auditAction(AuditAction.valueOf(createAuditDto.auditAction()))
                .auditDateTime(createAuditDto.auditDateTime())
                .build();
    }
}
