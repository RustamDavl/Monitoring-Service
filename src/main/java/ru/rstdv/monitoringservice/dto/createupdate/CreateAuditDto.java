package ru.rstdv.monitoringservice.dto.createupdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateAuditDto(
        String userId,
        String auditAction,
        LocalDateTime auditDateTime,
        String description
) {
}
