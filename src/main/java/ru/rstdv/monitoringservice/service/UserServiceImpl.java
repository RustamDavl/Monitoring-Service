package ru.rstdv.monitoringservice.service;

import lombok.RequiredArgsConstructor;
import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.entity.Audit;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.entity.embeddable.Role;
import ru.rstdv.monitoringservice.exception.EmailRegisteredException;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.UserMapper;
import ru.rstdv.monitoringservice.mapper.UserMapperImpl;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.repository.UserRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepositoryImpl;
    private final UserMapper userMapperImpl;
    private final AuditService auditServiceImpl;

    public ReadUserDto register(CreateUpdateUserDto createUpdateUserDto) {
        var maybeUser = userRepositoryImpl.findByEmail(createUpdateUserDto.email());
        if (maybeUser.isPresent())
            throw new EmailRegisteredException("user with such email already exists");

        var savedUser = userRepositoryImpl.save(userMapperImpl.toUser(createUpdateUserDto));

        auditServiceImpl.saveAudit(new CreateAuditDto(
                savedUser.getId().toString(),
                AuditAction.REGISTRATION.name(),
                LocalDateTime.now(),
                "user registered successfully"
        ));
        return userMapperImpl.toReadUserDto(savedUser);

    }

    public ReadUserDto authenticate(String email, String password) {
        var maybeUser = userRepositoryImpl.findByEmailAndPassword(email, password)
                .orElseThrow(
                        () -> new UserNotFoundException("bad credentials")
                );

        auditServiceImpl.saveAudit(new CreateAuditDto(
                maybeUser.getId().toString(),
                AuditAction.AUTHENTICATION.name(),
                LocalDateTime.now(),
                "user authenticated successfully"
        ));
        return userMapperImpl.toReadUserDto(maybeUser);
    }

    @Override
    public ReadUserDto findById(Long id) {
        return userRepositoryImpl.findById(id).map(userMapperImpl::toReadUserDto)
                .orElseThrow(() -> new UserNotFoundException("there is no user with id : " + id));
    }

    @Override
    public List<ReadUserDto> findAll() {
        return userRepositoryImpl.findAll()
                .stream()
                .map(userMapperImpl::toReadUserDto)
                .toList();
    }
}
