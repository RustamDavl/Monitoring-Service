package ru.rstdv.monitoringservice.intergration.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.entity.Audit;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.intergration.util.IntegrationTestBase;
import ru.rstdv.monitoringservice.mapper.AuditMapper;
import ru.rstdv.monitoringservice.mapper.AuditMapperImpl;
import ru.rstdv.monitoringservice.mapper.UserMapper;
import ru.rstdv.monitoringservice.mapper.UserMapperImpl;
import ru.rstdv.monitoringservice.repository.AuditRepository;
import ru.rstdv.monitoringservice.repository.AuditRepositoryImpl;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.repository.UserRepositoryImpl;
import ru.rstdv.monitoringservice.service.AuditService;
import ru.rstdv.monitoringservice.service.AuditServiceImpl;
import ru.rstdv.monitoringservice.util.LiquibaseUtil;
import ru.rstdv.monitoringservice.util.TestConnectionProvider;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


public class AuditServiceIT extends IntegrationTestBase {

    private AuditService auditService;
    private AuditRepository auditRepository;
    private AuditMapper auditMapper;

    private UserRepository userRepository;
    private UserMapper userMapper;


    @BeforeEach
    void setUp() {
        TestConnectionProvider testConnectionProvider = new TestConnectionProvider(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseUtil.start(testConnectionProvider);
        auditRepository = new AuditRepositoryImpl(testConnectionProvider);
        userMapper = new UserMapperImpl();
        userRepository = new UserRepositoryImpl(testConnectionProvider);
        auditMapper = new AuditMapperImpl(userMapper);
        auditService = new AuditServiceImpl(auditRepository, auditMapper, userRepository);
        userRepository = new UserRepositoryImpl(testConnectionProvider);
        auditService = new AuditServiceImpl(auditRepository, new AuditMapperImpl(userMapper), userRepository);

    }

    @AfterEach
    void clear() {
        LiquibaseUtil.dropAll();
    }

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
