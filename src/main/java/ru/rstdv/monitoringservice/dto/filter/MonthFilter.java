package ru.rstdv.monitoringservice.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


@Data
@AllArgsConstructor
public class MonthFilter implements Filter {

    private int monthNumber;
    @Override
    public int getMonthNumber() {
        return this.monthNumber;
    }
}
