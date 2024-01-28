package ru.rstdv.monitoringservice.unit.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rstdv.monitoringservice.entity.Audit;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.entity.embeddable.Role;
import ru.rstdv.monitoringservice.repository.AuditRepository;
import ru.rstdv.monitoringservice.repository.AuditRepositoryImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class AuditRepositoryTest {

    private final AuditRepository repository = AuditRepositoryImpl.getInstance();

    @AfterEach
    void clearDataBase() {
        AuditRepositoryImpl.clearDataBase();
    }

    @Test
    void findUserAudits() {
        repository.saveAudit(createAudit(AuditAction.REGISTRATION, 1L));
        repository.saveAudit(createAudit(AuditAction.AUTHENTICATION, 1L));
        repository.saveAudit(createAudit(AuditAction.THERMAL_METER_READING_SENDING, 1L));
        repository.saveAudit(createAudit(AuditAction.LOGOUT, 1L));
        repository.saveAudit(createAudit(AuditAction.REGISTRATION, 2L));
        repository.saveAudit(createAudit(AuditAction.REGISTRATION, 3L));
        repository.saveAudit(createAudit(AuditAction.WATER_METER_READING_SENDING, 2L));
        repository.saveAudit(createAudit(AuditAction.LOGOUT, 2L));


        var audits = repository.findUserAudits(1L);
        assertThat(audits).hasSize(4);

    }

    private Audit createAudit(AuditAction auditAction, Long userId) {
        return Audit.builder()
                .user(createUser(userId))
                .auditAction(auditAction)
                .build();
    }


    private User createUser(Long id) {
        return User.builder()
                .id(id)
                .firstname("Vi")
                .email("test")
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
    }
}
