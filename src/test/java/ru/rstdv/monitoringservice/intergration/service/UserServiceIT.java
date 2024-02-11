package ru.rstdv.monitoringservice.intergration.service;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.exception.EmailRegisteredException;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.factory.ServiceFactory;
import ru.rstdv.monitoringservice.factory.ServiceFactoryImpl;
import ru.rstdv.monitoringservice.mapper.AuditMapper;
import ru.rstdv.monitoringservice.mapper.AuditMapperImpl;
import ru.rstdv.monitoringservice.mapper.UserMapper;
import ru.rstdv.monitoringservice.repository.AuditRepositoryImpl;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.repository.UserRepositoryImpl;
import ru.rstdv.monitoringservice.service.AuditServiceImpl;
import ru.rstdv.monitoringservice.service.UserServiceImpl;
import ru.rstdv.monitoringservice.util.IntegrationTestBase;
import ru.rstdv.monitoringservice.service.AuditService;
import ru.rstdv.monitoringservice.service.UserService;
import ru.rstdv.monitoringservice.util.LiquibaseUtil;
import ru.rstdv.monitoringservice.util.TestConnectionProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceIT extends IntegrationTestBase {

    private UserService userService;
    private UserRepository userRepository;
    private AuditService auditService;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        connectionProvider = new TestConnectionProvider(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseUtil.start(connectionProvider);
        userRepository = new UserRepositoryImpl(connectionProvider);
        userMapper = UserMapper.INSTANCE;
        auditService = new AuditServiceImpl(new AuditRepositoryImpl(connectionProvider), AuditMapper.INSTANCE);
        userService = new UserServiceImpl(userRepository, userMapper, auditService);
    }

    @AfterEach
    void clear() {
        LiquibaseUtil.dropAll();
    }

    @DisplayName("register should pass")
    @Test
    void register_should_pass() {
        var createUpdateUserDto = getCreateUpdateUserDto("someUnique@gmail.com");
        var savedUser = userService.register(createUpdateUserDto);
        assertThat(savedUser.id()).isNotNull();
        assertThat(savedUser.email()).isEqualTo("someUnique@gmail.com");
    }

    @DisplayName("register should throw EmailRegisteredException")
    @Test
    void register_should_throw_EmailRegisteredException() {
        var createUpdateUserDto = getCreateUpdateUserDto("user2@gmail.com");
        assertThrows(EmailRegisteredException.class, () -> userService.register(createUpdateUserDto));
    }

    @DisplayName("authenticate should pass")
    @Test
    void authenticate_should_pass() {
        var createUpdateUserDto = getCreateUpdateUserDto("user2@gmail.com");
        var actualResult = userService.authenticate(createUpdateUserDto.email(), createUpdateUserDto.password());
        assertThat(actualResult).isNotNull();
    }

    @DisplayName("authenticate should throw UserNotFoundException")
    @Test
    void authenticate_should_throw_UserNotFoundException() {
        var createUpdateUserDto = getCreateUpdateUserDto("UN_REGISTERE@gmail.com");
        org.junit.jupiter.api.Assertions.assertThrows(
                UserNotFoundException.class, () -> userService.authenticate(createUpdateUserDto.email(), createUpdateUserDto.password())
        );
    }

    @DisplayName("find by id should pass")
    @Test
    void findById_should_pass() {
        var actualResult = userService.findById(2L);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.id()).isEqualTo("2");
        assertThat(actualResult.email()).isEqualTo("user2@gmail.com");
        assertThat(actualResult.personalAccount()).isEqualTo("999999999");
        assertThat(actualResult.address().getCity()).isEqualTo("Nigh city");
    }

    @DisplayName("find by id should throw UserNotFoundException")
    @Test
    void findById_should_throw_UserNotFoundException() {

        org.junit.jupiter.api.Assertions.assertThrows(
                UserNotFoundException.class, () -> userService.findById(100L)
        );
    }

    private CreateUpdateUserDto getCreateUpdateUserDto(String email) {
        return new CreateUpdateUserDto(
                "Vi",
                email,
                "pass",
                "999999999",
                "Nigh city",
                "jig-jig",
                "1"
        );
    }
}
