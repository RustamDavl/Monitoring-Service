package ru.rstdv.monitoringservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;

import java.time.LocalDateTime;

/**
 * сущность аудита, сохраняемая в таблицу базы данных
 *
 * @author RustamD
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Audit {

    private Long id;
    private User user;
    private AuditAction auditAction;
    private String description;
    private LocalDateTime auditDateTime;
}
