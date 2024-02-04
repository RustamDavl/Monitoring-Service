package ru.rstdv.monitoringservice.factory;

import ru.rstdv.monitoringservice.mapper.AuditMapper;
import ru.rstdv.monitoringservice.mapper.ThermalMeterMapper;
import ru.rstdv.monitoringservice.mapper.UserMapper;
import ru.rstdv.monitoringservice.mapper.WaterMeterMapper;

/**
 * интерфейс, предоставляющий все виды мапперов
 *
 * @author RustamD
 * @version 1.0
 */
public interface MapperFactory {
    UserMapper createUserMapper();
    ThermalMeterMapper createThermalMeterMapper();
    WaterMeterMapper createWaterMeterMapper();
    AuditMapper createAuditMapper();
}
