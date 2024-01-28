package ru.rstdv.monitoringservice.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class MonthFilterImpl implements MonthFilter {

    private int monthNumber;
    @Override
    public int getMonthNumber() {
        return this.monthNumber;
    }
}
