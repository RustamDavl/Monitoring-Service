package ru.rstdv.monitoringservice.dto.read;

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
