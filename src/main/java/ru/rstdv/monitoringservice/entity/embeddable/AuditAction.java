package ru.rstdv.monitoringservice.entity.embeddable;

/**
 * перечисление, представляющее действие пользователя для сохранения в аудите
 *
 * @author RustamD
 * @version 1.0
 */
public enum AuditAction {
    REGISTRATION,
    AUTHENTICATION,
    LOGOUT,
    WATER_METER_READING_SENDING,
    THERMAL_METER_READING_SENDING,
    GET_METER_READING_HISTORY,

    GET_ACTUAL_WATER_METER_READING,
    GET_ACTUAL_THERMAL_METER_READING
}
