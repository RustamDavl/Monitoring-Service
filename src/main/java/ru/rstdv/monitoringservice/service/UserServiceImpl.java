package ru.rstdv.monitoringservice.service;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.UserMapper;
import ru.rstdv.monitoringservice.mapper.UserMapperImpl;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.repository.UserRepositoryImpl;


public class UserServiceImpl implements UserService {


    private final UserRepository userRepositoryImpl = UserRepositoryImpl.getInstance();
    private final UserMapper userMapperImpl = UserMapperImpl.getInstance();
    private static final UserServiceImpl INSTANCE = new UserServiceImpl();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return INSTANCE;
    }

    public ReadUserDto register(CreateUpdateUserDto createUpdateUserDto) {
        var savedUser = userRepositoryImpl.save(userMapperImpl.toUser(createUpdateUserDto));
        return userMapperImpl.toReadUserDto(savedUser);

    }

    public ReadUserDto authenticate(String email, String password) {
        var maybeUser = userRepositoryImpl.findByEmailAndPassword(email, password)
                .orElseThrow(
                        () -> new UserNotFoundException("bad credentials")
                );
        return userMapperImpl.toReadUserDto(maybeUser);

    }
}
