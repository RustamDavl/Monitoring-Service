package ru.rstdv.monitoringservice.service;

import lombok.RequiredArgsConstructor;
import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.read.ReadAuditDto;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.AuditMapper;
import ru.rstdv.monitoringservice.repository.AuditRepository;
import ru.rstdv.monitoringservice.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepositoryImpl;
    private final AuditMapper auditMapperImpl;
    private final UserRepository userRepositoryImpl;

    @Override
    public List<ReadAuditDto> findUserAudits(String userId) {
        return auditRepositoryImpl.findByUserId(Long.valueOf(userId))
                .stream()
                .map(auditMapperImpl::toReadAuditDto)
                .toList();
    }

    @Override
    public void saveAudit(CreateAuditDto createAuditDto) {
        var user = userRepositoryImpl.findById(Long.valueOf(createAuditDto.userId()))
                .orElseThrow(() -> new UserNotFoundException("there is no user with id : " + createAuditDto.userId()));

        var auditToSave = auditMapperImpl.toAudit(createAuditDto, user);

        auditRepositoryImpl.save(auditToSave);
    }
}
