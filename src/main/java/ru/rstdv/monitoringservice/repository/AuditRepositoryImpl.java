package ru.rstdv.monitoringservice.repository;

import lombok.RequiredArgsConstructor;
import ru.rstdv.monitoringservice.entity.Audit;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.util.ConnectionProvider;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AuditRepositoryImpl implements AuditRepository {

    private final ConnectionProvider connectionProvider;
    private static final String FIND_AUDIT_BY_USER_ID_SQL = """
            SELECT id, user_id, audit_action, description, audit_date_time
            FROM audit
            WHERE user_id = ?;
            """;

    private static final String SAVE_SQL = """
            INSERT INTO audit(user_id, audit_action, description, audit_date_time)
            VALUES (?, ?, ?, ?);
            """;

    @Override
    public List<Audit> findByUserId(Long userId) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_AUDIT_BY_USER_ID_SQL)) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Audit> audits = new ArrayList<>();
            while (resultSet.next()) {
                audits.add(
                        Audit.builder()
                                .id(resultSet.getLong("id"))
                                .userId(resultSet.getLong("user_id"))
                                .auditAction(AuditAction.valueOf(resultSet.getString("audit_action")))
                                .description(resultSet.getString("description"))
                                .auditDateTime(resultSet.getTimestamp("audit_date_time").toLocalDateTime())
                                .build()
                );
            }
            return audits;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Audit audit) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setLong(1, audit.getUserId());
            preparedStatement.setString(2, audit.getAuditAction().name());
            preparedStatement.setString(3, audit.getDescription());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(audit.getAuditDateTime()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
