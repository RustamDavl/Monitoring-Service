package ru.rstdv.monitoringservice.mapper;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.Role;

public class UserMapper {

    private static final UserMapper INSTANCE = new UserMapper();

    private UserMapper() {
    }

    public static UserMapper getInstance() {
        return INSTANCE;
    }

    public ReadUserDto toReadUserDto(User user) {
        return new ReadUserDto(user.getId().toString(), user.getFirstname(), user.getEmail(),
                user.getAddress(), user.getPersonalAccount()
        );
    }


    public User toUser(CreateUpdateUserDto from) {
        return User.builder()
                .email(from.email())
                .address(Address.builder()
                        .city(from.city())
                        .street(from.street())
                        .houseNumber(from.houseNumber())
                        .build())
                .firstname(from.firstname())
                .role(Role.USER)
                .personalAccount(from.personalAccount())
                .password(from.password())
                .build();
    }
}
