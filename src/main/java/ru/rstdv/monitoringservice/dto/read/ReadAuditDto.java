package ru.rstdv.monitoringservice.dto.read;


/**
 * отображение класса Audit, возвращаемое пользователю
 *
 * @param id            идентификатор
 * @param readUserDto   отображение пользователя
 * @param auditDateTime время аудита
 * @param auditAction   действие аудита
 * @param description   дополнительное описание действия
 * @author RustamD
 * @version 1.0
 */
public record ReadAuditDto(String id,
                           ReadUserDto readUserDto,
                           String auditDateTime,
                           String auditAction,
                           String description) {
}
