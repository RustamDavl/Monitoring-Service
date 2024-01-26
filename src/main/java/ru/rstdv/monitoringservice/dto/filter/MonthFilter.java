package ru.rstdv.monitoringservice.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthFilter implements Filter{
    private Integer monthValue;
}
