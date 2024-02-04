package ru.rstdv.monitoringservice.dto.read;


/**
 * отображение класса Audit, возвращаемое пользователю
 *
 * @param id            идентификатор
 * @param userId        идентификатор пользователя
 * @param auditDateTime время аудита
 * @param auditAction   действие аудита
 * @param description   дополнительное описание действия
 * @author RustamD
 * @version 1.0
 */
public record ReadAuditDto(String id,
                           String userId,
                           String auditDateTime,
                           String auditAction,
                           String description) {
}
