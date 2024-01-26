package ru.rstdv.monitoringservice.entity;

import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WaterMeterReading {

    private Long id;

    private User user;

    private LocalDateTime dateOfMeterReading;

    private Integer coldWater;

    private Integer hotWater;

}
