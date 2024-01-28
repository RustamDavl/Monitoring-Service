package ru.rstdv.monitoringservice.dto.createupdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * класс, представляющий запрос на создание audit
 *
 * @param userId        идентификатор пользователя
 * @param auditAction   действие, произведенное пользователем
 * @param auditDateTime время создания аудита
 * @param description   дополнительное описание действий
 */
public record CreateAuditDto(
        String userId,
        String auditAction,
        LocalDateTime auditDateTime,
        String description
) {
}
