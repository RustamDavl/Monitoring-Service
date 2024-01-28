package ru.rstdv.monitoringservice.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * сущность показания счетчика тепла, сохраняемая в таблицу базы данных
 *
 * @author RustamD
 * @version 1.0
 */
@Getter
@SuperBuilder
@Setter
public class ThermalMeterReading extends MeterReading{

    private Long id;
    private Float gigaCalories;
}
