package ru.rstdv.monitoringservice.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.util.Date;

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

    protected Long userId;
    protected MeterReadingDate meterReadingDate;
}
