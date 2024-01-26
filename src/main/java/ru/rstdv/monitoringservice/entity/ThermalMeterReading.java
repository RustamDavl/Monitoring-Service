package ru.rstdv.monitoringservice.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;




@Getter
@SuperBuilder
public class ThermalMeterReading extends MeterReading{

    private Long id;
    private Float gigaCalories;
}
