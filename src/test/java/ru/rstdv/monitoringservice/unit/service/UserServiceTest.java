package ru.rstdv.monitoringservice.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.Role;
import ru.rstdv.monitoringservice.exception.EmailRegisteredException;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.UserMapper;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.service.AuditService;
import ru.rstdv.monitoringservice.service.UserServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepositoryImpl;
    @Mock
    private UserMapper userMapperImpl;
    @Mock
    private AuditService auditServiceImpl;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void register_should_pass() {
        var createUpdateUserDto = getCreateUpdateUserDto();
        var readUserDto = getReadUserDto();
        var user = getUser();

        doReturn(Optional.empty()).when(userRepositoryImpl).findByEmail(createUpdateUserDto.email());
        doReturn(user).when(userMapperImpl).toUser(createUpdateUserDto);
        doReturn(user).when(userRepositoryImpl).save(user);
        doReturn(readUserDto).when(userMapperImpl).toReadUserDto(user);

        var actualResult = userServiceImpl.register(createUpdateUserDto);
        assertThat(actualResult).isEqualTo(readUserDto);
    }

    @Test
    void register_should_throw_EmailRegisteredException() {
        var createUpdateUserDto = getCreateUpdateUserDto();
        var user = getUser();
        doReturn(Optional.of(user)).when(userRepositoryImpl).findByEmail(createUpdateUserDto.email());
        assertThrows(EmailRegisteredException.class, () -> userServiceImpl.register(createUpdateUserDto));
    }

    @Test
    void authenticate_should_pass() {
        var user = getUser();
        var readUserDto = getReadUserDto();

        doReturn(Optional.of(user)).when(userRepositoryImpl).findByEmailAndPassword(user.getEmail(), user.getPassword());
        doReturn(readUserDto).when(userMapperImpl).toReadUserDto(user);

        var actualResult = userServiceImpl.authenticate(user.getEmail(), user.getPassword());
        assertThat(actualResult).isEqualTo(readUserDto);
    }

    @Test
    void authenticate_should_throw_UserNotFoundException() {
        var user = getUser();
        doReturn(Optional.empty()).when(userRepositoryImpl).findByEmailAndPassword(user.getEmail(), user.getPassword());

        org.junit.jupiter.api.Assertions.assertThrows(
                UserNotFoundException.class, () -> userServiceImpl.authenticate(user.getEmail(), user.getPassword())
        );
    }

    @Test
    void findById_should_pass() {
        var user = getUser();
        var readUserDto = getReadUserDto();
        doReturn(Optional.of(user)).when(userRepositoryImpl).findById(1L);
        doReturn(readUserDto).when(userMapperImpl).toReadUserDto(user);
        var actualResult = userServiceImpl.findById(1L);

        assertThat(actualResult).isEqualTo(readUserDto);
    }

    @Test
    void findById_should_throw_UserNotFoundException() {

        doReturn(Optional.empty()).when(userRepositoryImpl).findById(1L);

        org.junit.jupiter.api.Assertions.assertThrows(
                UserNotFoundException.class, () -> userServiceImpl.findById(1L)
        );
    }

    private User getUser() {
        return User.builder()
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
    }

    private ReadUserDto getReadUserDto() {
        return new ReadUserDto(
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
        );
    }

    private CreateUpdateUserDto getCreateUpdateUserDto() {
        return new CreateUpdateUserDto(
                "Vi",
                "vivi@gmail.com",
                "pass",
                "999999999",
                "Nigh city",
                "jig-jig",
                "1"
        );
    }

}

