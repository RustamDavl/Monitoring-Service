package ru.rstdv.monitoringservice.entity.embeddable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Year;


/**
 * встариваемый класс, представляющий дату отправки показателей счетчика
 *
 * @author RustamD
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MeterReadingDate {
    private Year year;
    private Integer month;
    private Integer monthDay;
}
