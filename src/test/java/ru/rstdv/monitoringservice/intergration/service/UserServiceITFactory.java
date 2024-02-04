package ru.rstdv.monitoringservice.intergration.service;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.exception.EmailRegisteredException;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.util.IntegrationTestBase;
import ru.rstdv.monitoringservice.mapper.AuditMapperImpl;
import ru.rstdv.monitoringservice.mapper.UserMapper;
import ru.rstdv.monitoringservice.mapper.UserMapperImpl;
import ru.rstdv.monitoringservice.repository.AuditRepositoryImpl;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.repository.UserRepositoryImpl;
import ru.rstdv.monitoringservice.service.AuditService;
import ru.rstdv.monitoringservice.service.AuditServiceImpl;
import ru.rstdv.monitoringservice.service.UserService;
import ru.rstdv.monitoringservice.service.UserServiceImpl;
import ru.rstdv.monitoringservice.util.LiquibaseUtil;
import ru.rstdv.monitoringservice.util.TestConnectionProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceITFactory extends IntegrationTestBase {

    private UserService userService;
    private UserRepository userRepository;
    private AuditService auditService;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        TestConnectionProvider testConnectionProvider = new TestConnectionProvider(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseUtil.start(testConnectionProvider);
        userRepository = new UserRepositoryImpl(testConnectionProvider);
        userMapper = new UserMapperImpl();
        auditService = new AuditServiceImpl(new AuditRepositoryImpl(testConnectionProvider), new AuditMapperImpl(), userRepository);
        userService = new UserServiceImpl(userRepository, new UserMapperImpl(), auditService);

    }

    @AfterEach
    void clear() {
        LiquibaseUtil.dropAll();
    }

    @Test
    void register_should_pass() {
        var createUpdateUserDto = getCreateUpdateUserDto("someUnique@gmail.com");
        var savedUser = userService.register(createUpdateUserDto);
        assertThat(savedUser.id()).isNotNull();
        assertThat(savedUser.email()).isEqualTo("someUnique@gmail.com");

    }

    @Test
    void register_should_throw_EmailRegisteredException() {
        var createUpdateUserDto = getCreateUpdateUserDto("user2@gmail.com");
        assertThrows(EmailRegisteredException.class, () -> userService.register(createUpdateUserDto));
    }

    @Test
    void authenticate_should_pass() {
        var createUpdateUserDto = getCreateUpdateUserDto("user2@gmail.com");
        var actualResult = userService.authenticate(createUpdateUserDto.email(), createUpdateUserDto.password());
        assertThat(actualResult).isNotNull();
    }

    @Test
    void authenticate_should_throw_UserNotFoundException() {
        var createUpdateUserDto = getCreateUpdateUserDto("UN_REGISTERE@gmail.com");
        org.junit.jupiter.api.Assertions.assertThrows(
                UserNotFoundException.class, () -> userService.authenticate(createUpdateUserDto.email(), createUpdateUserDto.password())
        );
    }

    @Test
    void findById_should_pass() {
        var actualResult = userService.findById(2L);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult.id()).isEqualTo("2");
        assertThat(actualResult.email()).isEqualTo("user2@gmail.com");
        assertThat(actualResult.personalAccount()).isEqualTo("999999999");
        assertThat(actualResult.address().getCity()).isEqualTo("Nigh city");
    }

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
