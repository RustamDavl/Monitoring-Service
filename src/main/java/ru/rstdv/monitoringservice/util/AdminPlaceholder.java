package ru.rstdv.monitoringservice.util;

import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.Role;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.repository.UserRepositoryImpl;

public class AdminPlaceholder {

    private static final UserRepository userRepositoryImpl = UserRepositoryImpl.getInstance();

    public static void addAdmin() {
        var admin = User.builder()
                .address(Address.builder()
                        .city("Admin city")
                        .street("Admin street")
                        .houseNumber("9999")
                        .build()
                )
                .email("admin@gmail.com")
                .password("admin")
                .firstname("admin")
                .role(Role.ADMIN)
                .personalAccount("999999999")
                .build();
       userRepositoryImpl.save(admin);
    }
}
