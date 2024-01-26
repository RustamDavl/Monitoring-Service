package ru.rstdv.monitoringservice.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;



@SuperBuilder
@Data
public class WaterMeterReading extends MeterReading {

    private Long id;

    private Integer coldWater;

    private Integer hotWater;

}
