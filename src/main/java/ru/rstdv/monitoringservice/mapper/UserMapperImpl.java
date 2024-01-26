package ru.rstdv.monitoringservice.mapper;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.Role;

public class UserMapperImpl implements UserMapper {

    private static final UserMapperImpl INSTANCE = new UserMapperImpl();

    private UserMapperImpl() {
    }


    public static UserMapperImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public ReadUserDto toReadUserDto(User user) {
        return new ReadUserDto(user.getId().toString(), user.getFirstname(), user.getEmail(),
                user.getAddress(), user.getPersonalAccount()
        );
    }


    @Override
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
