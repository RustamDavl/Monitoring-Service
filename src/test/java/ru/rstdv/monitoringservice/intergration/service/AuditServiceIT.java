package ru.rstdv.monitoringservice.intergration.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.entity.Audit;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.factory.RepositoryFactory;
import ru.rstdv.monitoringservice.factory.RepositoryFactoryImpl;
import ru.rstdv.monitoringservice.factory.ServiceFactory;
import ru.rstdv.monitoringservice.factory.ServiceFactoryImpl;
import ru.rstdv.monitoringservice.mapper.AuditMapper;
import ru.rstdv.monitoringservice.mapper.UserMapper;
import ru.rstdv.monitoringservice.repository.AuditRepositoryImpl;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.repository.UserRepositoryImpl;
import ru.rstdv.monitoringservice.service.AuditServiceImpl;
import ru.rstdv.monitoringservice.util.IntegrationTestBase;
import ru.rstdv.monitoringservice.repository.AuditRepository;
import ru.rstdv.monitoringservice.service.AuditService;
import ru.rstdv.monitoringservice.util.LiquibaseUtil;
import ru.rstdv.monitoringservice.util.TestConnectionProvider;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


public class AuditServiceIT extends IntegrationTestBase {

    private AuditService auditService;
    private AuditRepository auditRepository;
    private AuditMapper auditMapper;

    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        connectionProvider = new TestConnectionProvider(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseUtil.start(connectionProvider);
        auditRepository = new AuditRepositoryImpl(connectionProvider);
        userRepository = new UserRepositoryImpl(connectionProvider);
        auditMapper = AuditMapper.INSTANCE;
        auditService = new AuditServiceImpl(auditRepository, auditMapper);
        userRepository = new UserRepositoryImpl(connectionProvider);
        auditService = new AuditServiceImpl(auditRepository, auditMapper);
    }

    @AfterEach
    void clear() {
        LiquibaseUtil.dropAll();
    }

    @DisplayName("save")
    @Test
    void save() {
        CreateAuditDto createAuditDto = new CreateAuditDto(
                "4",
                AuditAction.AUTHENTICATION.name(),
                LocalDateTime.now(),
                "user with id : 4 was authenticated"
        );
        auditService.saveAudit(createAuditDto);
        var audits = auditRepository.findByUserId(4L);

        var actions = audits.stream()
                .map(Audit::getAuditAction)
                .toList();
        assertThat(actions).contains(AuditAction.REGISTRATION, AuditAction.AUTHENTICATION);
    }

    @DisplayName("find by user id")
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
}
