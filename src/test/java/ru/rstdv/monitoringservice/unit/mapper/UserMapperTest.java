package ru.rstdv.monitoringservice.unit.mapper;

import org.assertj.core.api.Assertions;
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

    private final UserMapper userMapperImpl = UserMapperImpl.getInstance();

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
        var actualResult = userMapperImpl.toUser(createUpdateUserDto);

        var expectedResult = User.builder()
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

        assertThat(actualResult).isEqualTo(expectedResult);
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

        var actualResult = userMapperImpl.toReadUserDto(user);

        var expectedResult = new ReadUserDto(
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
        assertThat(actualResult.id()).isEqualTo(expectedResult.id());
        assertThat(actualResult.firstname()).isEqualTo(expectedResult.firstname());
        assertThat(actualResult.email()).isEqualTo(expectedResult.email());
        assertThat(actualResult.address()).isEqualTo(expectedResult.address());
        assertThat(actualResult.role()).isEqualTo(expectedResult.role());
        assertThat(actualResult.personalAccount()).isEqualTo(expectedResult.personalAccount());

    }
}
