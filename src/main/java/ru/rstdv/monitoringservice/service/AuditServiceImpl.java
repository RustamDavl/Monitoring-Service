package ru.rstdv.monitoringservice.service;

import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.read.ReadAuditDto;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.AuditMapper;
import ru.rstdv.monitoringservice.mapper.AuditMapperImpl;
import ru.rstdv.monitoringservice.repository.AuditRepository;
import ru.rstdv.monitoringservice.repository.AuditRepositoryImpl;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.repository.UserRepositoryImpl;

import java.util.List;

public class AuditServiceImpl implements AuditService {
    private final AuditRepository auditRepositoryImpl = AuditRepositoryImpl.getInstance();
    private final AuditMapper auditMapperImpl = AuditMapperImpl.getInstance();
    private final UserRepository userRepositoryImpl = UserRepositoryImpl.getInstance();
    private static final AuditServiceImpl INSTANCE = new AuditServiceImpl();


    private AuditServiceImpl() {
    }

    public static AuditServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<ReadAuditDto> findUserAudits(String userId) {
        return auditRepositoryImpl.findUserAudits(Long.valueOf(userId))
                .stream()
                .map(auditMapperImpl::toReadAuditDto)
                .toList();
    }

    @Override
    public void saveAudit(CreateAuditDto createAuditDto) {
        var user = userRepositoryImpl.findById(Long.valueOf(createAuditDto.userId()))
                .orElseThrow(() -> new UserNotFoundException("there is no user with id : " + createAuditDto.userId()));

        var auditToSave = auditMapperImpl.toAudit(createAuditDto, user);

        auditRepositoryImpl.saveAudit(auditToSave);
    }
}
