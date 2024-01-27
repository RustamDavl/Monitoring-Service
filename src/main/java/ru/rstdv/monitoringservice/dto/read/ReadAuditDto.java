package ru.rstdv.monitoringservice.dto.read;

public record ReadAuditDto(String id,
                           ReadUserDto readUserDto,
                           String auditDateTime,
                           String auditAction,
                           String description) {
}
