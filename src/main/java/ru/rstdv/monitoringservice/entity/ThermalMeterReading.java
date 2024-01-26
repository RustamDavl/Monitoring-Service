package ru.rstdv.monitoringservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ThermalMeterReading {

    private Long id;
    private User user;
    private LocalDateTime dateOfMeterReading;
    private Float gigaCalories;
}
