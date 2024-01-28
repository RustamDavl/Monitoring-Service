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
                .user(User.builder()
                        .id(1L)
                        .firstname("Vi")
                        .email("vivi@gmail.com")
                        .password("pass")
                        .personalAccount("999999999")
                        .address(
                                Address.builder()
                                        .city("Nigh city")
                                        .street("jig-jig")
                                        .houseNumber("1")
                                        .build()
                        )
                        .role(Role.USER)
                        .build())
                .auditAction(AuditAction.AUTHENTICATION)
                .auditDateTime(auditLocalDateTime)
                .description("descr")
                .build();

        var actualResult = auditMapperImpl.toReadAuditDto(audit);
        var expectedResult = new ReadAuditDto(
                "1",
                new ReadUserDto(
                        "1",
                        "Vi",
                        "vivi@gmail.com",
                        Address.builder()
                                .city("Nigh city")
                                .street("jig-jig")
                                .houseNumber("1")
                                .build(),
                        "USER",
                        "999999999"
                ),
                auditLocalDateTime.toString(),
                "AUTHENTICATION",
                "descr"

        );
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void toAudit() {
        var auditLocalDateTime = LocalDateTime.now();
        var user = User.builder()
                .id(1L)
                .firstname("Vi")
                .email("vivi@gmail.com")
                .password("pass")
                .personalAccount("999999999")
                .address(
                        Address.builder()
                                .city("Nigh city")
                                .street("jig-jig")
                                .houseNumber("1")
                                .build()
                )
                .role(Role.USER)
                .build();
        var createAuditDto = new CreateAuditDto(
                "1",
                "AUTHENTICATION",
                auditLocalDateTime,
                "descr"
        );
        var actualResult = auditMapperImpl.toAudit(createAuditDto, user);

        var expectedResult = Audit.builder()
                .user(user)
                .auditAction(AuditAction.AUTHENTICATION)
                .auditDateTime(auditLocalDateTime)
                .description("descr")
                .build();

        assertThat(actualResult.getId()).isEqualTo(expectedResult.getId());
        assertThat(actualResult.getUser()).isEqualTo(expectedResult.getUser());
        assertThat(actualResult.getAuditDateTime()).isEqualTo(expectedResult.getAuditDateTime());
        assertThat(actualResult.getDescription()).isEqualTo(expectedResult.getDescription());
        assertThat(actualResult.getAuditAction()).isEqualTo(expectedResult.getAuditAction());
    }

}
