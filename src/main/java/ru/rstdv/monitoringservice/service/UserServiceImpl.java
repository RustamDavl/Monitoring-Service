package ru.rstdv.monitoringservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import ru.rstdv.monitoringservice.aspect.annotation.Auditable;
import ru.rstdv.monitoringservice.aspect.annotation.Loggable;
import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.exception.EmailRegisteredException;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.UserMapper;
import ru.rstdv.monitoringservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Loggable
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuditService auditService;

    @Auditable
    public ReadUserDto register(CreateUpdateUserDto createUpdateUserDto) {
        var maybeUser = userRepository.findByEmail(createUpdateUserDto.email());
        if (maybeUser.isPresent())
            throw new EmailRegisteredException("user with such email already exists");

        var savedUser = userRepository.save(userMapper.toUser(createUpdateUserDto));

        return userMapper.toReadUserDto(savedUser);
    }

    @Auditable
    public ReadUserDto authenticate(String email, String password) {
        var maybeUser = userRepository.findByEmailAndPassword(email, password)
                .orElseThrow(
                        () -> new UserNotFoundException("bad credentials")
                );
        return userMapper.toReadUserDto(maybeUser);
    }

    @Override
    public ReadUserDto findById(Long id) {
        return userRepository.findById(id).map(userMapper::toReadUserDto)
                .orElseThrow(() -> new UserNotFoundException("there is no user with id : " + id));
    }

    @Override
    public List<ReadUserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toReadUserDto)
                .toList();
    }
}
