package ru.rstdv.monitoringservice.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * базовый класс показания счетчика с общими полями.
 * не является сущностью.
 * взаимодействовать с базой данных по данному базовому классу нельзя
 *
 * @author RustamD
 * @version 1.0
 */
@Getter
@Setter
@SuperBuilder
public abstract class MeterReading {

    protected User user;

    protected LocalDateTime dateOfMeterReading;
}
