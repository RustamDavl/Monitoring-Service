package ru.rstdv.monitoringservice.entity.embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Year;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MeterReadingDate {
    private Year year;
    private Integer month;
    private Integer monthDay;
}
