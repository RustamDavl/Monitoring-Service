package ru.rstdv.monitoringservice.intergration.repository;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rstdv.monitoringservice.entity.Audit;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.util.IntegrationTestBase;
import ru.rstdv.monitoringservice.repository.*;
import ru.rstdv.monitoringservice.util.LiquibaseUtil;
import ru.rstdv.monitoringservice.util.TestConnectionProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class AuditRepositoryITFactory extends IntegrationTestBase {
    private AuditRepository auditRepository;
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        TestConnectionProvider testConnectionProvider = new TestConnectionProvider(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );

        LiquibaseUtil.start(testConnectionProvider);
        auditRepository = new AuditRepositoryImpl(testConnectionProvider);
        userRepository = new UserRepositoryImpl(testConnectionProvider);
    }

    @AfterEach
    void clear() {
        LiquibaseUtil.dropAll();
    }

    @Test
    void save() {
        var user = userRepository.findById(2L).get();
        auditRepository.save(createAudit(AuditAction.AUTHENTICATION, user.getId()));
        auditRepository.save(createAudit(AuditAction.WATER_METER_READING_SENDING, user.getId()));
        auditRepository.save(createAudit(AuditAction.THERMAL_METER_READING_SENDING, user.getId()));
        auditRepository.save(createAudit(AuditAction.LOGOUT, user.getId()));

        var audits = auditRepository.findByUserId(user.getId());
        var actions = audits.stream()
                .map(Audit::getAuditAction)
                .toList();
        assertThat(actions).contains(
                AuditAction.AUTHENTICATION, AuditAction.WATER_METER_READING_SENDING,
                AuditAction.THERMAL_METER_READING_SENDING, AuditAction.LOGOUT
        );

    }

    @Test
    void findByUserId() {
        var audits = auditRepository.findByUserId(3L);
        var actions = audits.stream()
                .map(Audit::getAuditAction)
                .toList();
        assertThat(actions).contains(
                AuditAction.AUTHENTICATION, AuditAction.REGISTRATION, AuditAction.LOGOUT
        );

    }

    private Audit createAudit(AuditAction auditAction, Long userId) {
        return Audit.builder()
                .userId(userId)
                .auditAction(auditAction)
                .auditDateTime(LocalDateTime.now())
                .description(auditAction.name() + " action was registered")
                .build();
    }

}

