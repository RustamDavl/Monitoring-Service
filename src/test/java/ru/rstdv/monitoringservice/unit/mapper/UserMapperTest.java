package ru.rstdv.monitoringservice.unit.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.Role;
import ru.rstdv.monitoringservice.mapper.UserMapper;
import ru.rstdv.monitoringservice.mapper.UserMapperImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    void toUser() {
        var createUpdateUserDto = new CreateUpdateUserDto(
                "Vi",
                "vivi@gmail.com",
                "pass",
                "999999999",
                "Nigh city",
                "jig-jig",
                "1"
        );
        var actualResult = userMapper.toUser(createUpdateUserDto);

        assertThat(actualResult.getEmail()).isEqualTo(createUpdateUserDto.email());
        assertThat(actualResult.getPassword()).isEqualTo(createUpdateUserDto.password());
        assertThat(actualResult.getAddress().getCity()).isEqualTo(createUpdateUserDto.city());
        assertThat(actualResult.getAddress().getStreet()).isEqualTo(createUpdateUserDto.street());
        assertThat(actualResult.getAddress().getHouseNumber()).isEqualTo(createUpdateUserDto.houseNumber());
        assertThat(actualResult.getPersonalAccount()).isEqualTo(createUpdateUserDto.personalAccount());
    }

    @Test
    void toReadUserDto() {
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

        var actualResult = userMapper.toReadUserDto(user);

        assertThat(actualResult.id()).isEqualTo(user.getId().toString());
        assertThat(actualResult.firstname()).isEqualTo(user.getFirstname());
        assertThat(actualResult.email()).isEqualTo(user.getEmail());
        assertThat(actualResult.address()).isEqualTo(user.getAddress());
        assertThat(actualResult.role()).isEqualTo(user.getRole().name());
        assertThat(actualResult.personalAccount()).isEqualTo(user.getPersonalAccount());

    }
}
