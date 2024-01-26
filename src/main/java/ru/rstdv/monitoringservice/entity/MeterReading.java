package ru.rstdv.monitoringservice.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public abstract class MeterReading {

    protected User user;

    protected LocalDateTime dateOfMeterReading;
}
