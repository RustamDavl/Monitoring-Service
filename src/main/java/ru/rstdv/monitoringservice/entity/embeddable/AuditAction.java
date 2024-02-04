package ru.rstdv.monitoringservice.entity.embeddable;

/**
 * перечисление, отображающее действие пользователя
 *
 * @author RustamD
 * @version 1.0
 */
public enum AuditAction {

    /**
     * регистрация пользователя
     */
    REGISTRATION,

    /**
     * аутентификация пользователя
     */
    AUTHENTICATION,
    /**
     * выход
     */
    LOGOUT,
    /**
     * отправка показателей счетчика воды
     */
    WATER_METER_READING_SENDING,
    /**
     * отправка показателей счетчика тепла
     */
    THERMAL_METER_READING_SENDING,
    /**
     * получение истории показателей счетчика тепла
     */
    GET_THERMAL_READING_HISTORY,
    /**
     * получение показателей счетчика тепла за месяц
     */
    GET_THERMAL_READING_BY_MONTH,
    /**
     * получение истории показателей счетчика воды
     */
    GET_WATER_READING_HISTORY,
    /**
     * получение показателей счетчика воды за месяц
     */
    GET_WATER_READING_BY_MONTH,
    /**
     * получение последнего показания счетчика воды
     */
    GET_ACTUAL_WATER_METER_READING,
    /**
     * получение последнего показания счетчика тепла
     */
    GET_ACTUAL_THERMAL_METER_READING
}
