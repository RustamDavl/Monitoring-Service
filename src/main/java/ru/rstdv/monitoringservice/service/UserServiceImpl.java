package ru.rstdv.monitoringservice.service;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.UserMapper;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.repository.UserRepositoryImpl;


public class UserServiceImpl implements UserService {


    private static final UserRepository userRepositoryImpl = UserRepositoryImpl.getInstance();
    private static final UserMapper userMapper = UserMapper.getInstance();
    private static final UserServiceImpl INSTANCE = new UserServiceImpl();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return INSTANCE;
    }

    public ReadUserDto register(CreateUpdateUserDto createUpdateUserDto) {
        var savedUser = userRepositoryImpl.save(userMapper.toUser(createUpdateUserDto));
        return userMapper.toReadUserDto(savedUser);

    }

    public ReadUserDto authenticate(String email, String password) {
        var maybeUser = userRepositoryImpl.findByEmailAndPassword(email, password)
                .orElseThrow(
                        () -> new UserNotFoundException("there is no user with such email and password")
                );
        return userMapper.toReadUserDto(maybeUser);

    }
}
