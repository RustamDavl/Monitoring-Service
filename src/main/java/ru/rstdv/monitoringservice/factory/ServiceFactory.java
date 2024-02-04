package ru.rstdv.monitoringservice.factory;

import ru.rstdv.monitoringservice.service.*;

/**
 * интерфейс, предоставляющий все виды сервисов
 *
 * @author RustamD
 * @version 1.0
 */
public interface ServiceFactory {

    UserService createUserService();

    WaterMeterReadingServiceImpl createWaterMeterReadingService();

    ThermalMeterReadingServiceImpl createThermalMeterReadingService();

    AuditService createAuditService();
}
