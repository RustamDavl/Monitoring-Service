package ru.rstdv.monitoringservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.read.ReadAuditDto;
import ru.rstdv.monitoringservice.mapper.AuditMapper;
import ru.rstdv.monitoringservice.repository.AuditRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper;

    @Override
    public List<ReadAuditDto> findUserAudits(String userId) {
        return auditRepository.findByUserId(Long.valueOf(userId))
                .stream()
                .map(auditMapper::toReadAuditDto)
                .toList();
    }

    @Override
    public void saveAudit(CreateAuditDto createAuditDto) {
        var auditToSave = auditMapper.toAudit(createAuditDto);
        auditRepository.save(auditToSave);
    }
}
