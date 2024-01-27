package ru.rstdv.monitoringservice.service;

import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.read.ReadAuditDto;

import java.util.List;

public interface AuditService {

    List<ReadAuditDto> findUserAudits(String userId);

    void saveAudit(CreateAuditDto createAuditDto);
}
