package ru.rstdv.monitoringservice.dto.createupdate;

import ru.rstdv.monitoringservice.entity.User;

public record CreateUpdateWaterMeterReadingDto(String userId, String coldWater, String hotWater) {
}
