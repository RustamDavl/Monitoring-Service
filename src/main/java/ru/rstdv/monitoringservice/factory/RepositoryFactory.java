package ru.rstdv.monitoringservice.factory;

import ru.rstdv.monitoringservice.repository.*;

/**
 * интерфейс, предоставляющий все виды репозиторие
 *
 * @author RustamD
 * @version 1.0
 */
public interface RepositoryFactory {
    UserRepository createUserRepository();
    ThermalMeterReadingRepositoryImpl createThermalMeterReadingRepository();
    WaterMeterReadingRepositoryImpl createWaterMeterReadingRepository();
    AuditRepository createAuditRepository();
}
