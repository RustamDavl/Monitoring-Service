package ru.rstdv.monitoringservice.repository;

import ru.rstdv.monitoringservice.entity.Audit;


import java.util.List;


public interface AuditRepository {

    List<Audit> findUserAudits(Long userId);

    void saveAudit(Audit audit);

}
