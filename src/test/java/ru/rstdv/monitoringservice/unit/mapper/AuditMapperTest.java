package ru.rstdv.monitoringservice.unit.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.read.ReadAuditDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.entity.Audit;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.entity.embeddable.Role;
import ru.rstdv.monitoringservice.mapper.AuditMapper;
import ru.rstdv.monitoringservice.mapper.AuditMapperImpl;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class AuditMapperTest {

    private final AuditMapper auditMapperImpl = AuditMapperImpl.getInstance();

    @Test
    void toReadAuditDto() {
        var auditLocalDateTime = LocalDateTime.now();
        var audit = Audit.builder()
                .id(1L)
                .userId(1L)
                .auditAction(AuditAction.AUTHENTICATION)
                .auditDateTime(auditLocalDateTime)
                .description("descr")
                .build();

        var actualResult = auditMapperImpl.toReadAuditDto(audit);

        assertThat(actualResult.id()).isEqualTo(audit.getId().toString());
        assertThat(actualResult.userId()).isEqualTo(audit.getUserId().toString());
        assertThat(actualResult.auditAction()).isEqualTo(audit.getAuditAction().name());
        assertThat(actualResult.description()).isEqualTo(audit.getDescription());
        assertThat(actualResult.id()).isEqualTo(audit.getId().toString());
    }

    @Test
    void toAudit() {
        var auditLocalDateTime = LocalDateTime.now();
        var user = User.builder()
                .id(1L)
                .build();
        var createAuditDto = new CreateAuditDto(
                user.getId().toString(),
                AuditAction.REGISTRATION.name(),
                auditLocalDateTime,
                "descr"
        );
        var actualResult = auditMapperImpl.toAudit(createAuditDto, user);

        assertThat(actualResult.getUserId()).isEqualTo(Long.valueOf(createAuditDto.userId()));
        assertThat(actualResult.getAuditAction().name()).isEqualTo(createAuditDto.auditAction());
        assertThat(actualResult.getAuditDateTime()).isEqualTo(createAuditDto.auditDateTime());
    }

}
