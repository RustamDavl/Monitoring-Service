package ru.rstdv.monitoringservice.dto.read;

/**
 * отображение класса ThermalMeterReading, возвращаемое пользователю
 *
 * @param id                 идентификатор
 * @param readUserDto        отображение пользователя
 * @param gigaCalories       значение в Гкал
 * @param dateOfMeterReading дата внесения показания
 * @author RustamD
 * @version 1.0
 */
public record ReadThermalMeterReadingDto(
        String id,
        ReadUserDto readUserDto,
        String gigaCalories,
        String dateOfMeterReading

) {
    public String toString() {
        return """
                                
                id : %s 
                -----------------------
                giga_calories : %s
                -----------------------
                date_of_meter_reading : %s
                                
                """.formatted(id, gigaCalories, dateOfMeterReading);
    }
}
