package ru.rstdv.monitoringservice.entity.embeddable;

/**
 * перечисление, отображающее роль пользователя
 *
 * @author RustamD
 * @version 1.0
 */
public enum Role {
    /**
     * пользователь без прав даминистратора
     */
    USER,
    /**
     * пользователь с привилегированными правами
     */
    ADMIN
}
