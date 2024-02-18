package ru.rstdv.monitoringservice.intergration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.entity.Audit;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.mapper.AuditMapperImpl;
import ru.rstdv.monitoringservice.repository.*;
import ru.rstdv.monitoringservice.service.AuditServiceImpl;
import ru.rstdv.monitoringservice.util.IntegrationTestBase;
import ru.rstdv.monitoringservice.service.AuditService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ContextConfiguration(classes = {
        AuditServiceImpl.class,
        AuditRepositoryImpl.class,
        AuditMapperImpl.class
})
public class AuditServiceIT extends IntegrationTestBase {

    private final AuditService auditService;
    private final AuditRepository auditRepository;


//
//    @BeforeEach
//    void setUp() {
//        connectionProvider = new TestConnectionProvider(
//                container.getJdbcUrl(),
//                container.getUsername(),
//                container.getPassword()
//        );
//        LiquibaseUtil.start(connectionProvider);
//        auditRepository = new AuditRepositoryImpl(connectionProvider);
//        userRepository = new UserRepositoryImpl(connectionProvider);
//        auditService = new AuditServiceImpl(auditRepository, auditMapper);
//        userRepository = new UserRepositoryImpl(connectionProvider);
//        auditService = new AuditServiceImpl(auditRepository, auditMapper);
//    }

//    @AfterEach
//    void clear() {
//        LiquibaseUtil.dropAll();
//    }

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
