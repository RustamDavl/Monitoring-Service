package ru.rstdv.monitoringservice.unit.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.entity.Audit;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.mapper.AuditMapper;
import ru.rstdv.monitoringservice.mapper.AuditMapperImpl;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = AuditMapperImpl.class)
@ExtendWith(SpringExtension.class)
public class AuditMapperTest {

    @Autowired
    private AuditMapper auditMapper;


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

        var actualResult = auditMapper.toReadAuditDto(audit);

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
        var actualResult = auditMapper.toAudit(createAuditDto);

        assertThat(actualResult.getUserId()).isEqualTo(Long.valueOf(createAuditDto.userId()));
        assertThat(actualResult.getAuditAction().name()).isEqualTo(createAuditDto.auditAction());
        assertThat(actualResult.getAuditDateTime()).isEqualTo(createAuditDto.auditDateTime());
    }

}
