package ru.rstdv.monitoringservice.repository;

import ru.rstdv.monitoringservice.entity.Audit;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.util.DataBaseTable;

import java.util.List;
import java.util.Objects;

public class AuditRepositoryImpl implements AuditRepository {

    private final UserRepository userRepositoryImpl = UserRepositoryImpl.getInstance();
    private static final AuditRepositoryImpl INSTANCE = new AuditRepositoryImpl();
    private static final DataBaseTable<Audit> AUDIT_TABLE = new DataBaseTable<>();


    private AuditRepositoryImpl() {
    }

    public static AuditRepositoryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Audit> findUserAudits(Long userId) {
        return AUDIT_TABLE.GET_ALL().stream()
                .filter(audit -> Objects.equals(audit.getUser().getId(), userId))
                .toList();
    }

    @Override
    public void saveAudit(Audit audit) {

        AUDIT_TABLE.INSERT(audit);
    }
}
