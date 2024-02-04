package ru.rstdv.monitoringservice.factory;

import ru.rstdv.monitoringservice.repository.*;
import ru.rstdv.monitoringservice.util.CommonConnectionProvider;
import ru.rstdv.monitoringservice.util.ConnectionProvider;

public class RepositoryFactoryImpl implements RepositoryFactory {
    private ConnectionProvider connectionProvider;
    public RepositoryFactoryImpl() {
        connectionProvider = new CommonConnectionProvider();
    }
    @Override
    public UserRepository createUserRepository() {
        return new UserRepositoryImpl(connectionProvider);
    }
    @Override
    public ThermalMeterReadingRepositoryImpl createThermalMeterReadingRepository() {
        return new ThermalMeterReadingRepositoryImpl(connectionProvider);
    }
    @Override
    public WaterMeterReadingRepositoryImpl createWaterMeterReadingRepository() {
        return new WaterMeterReadingRepositoryImpl(connectionProvider);
    }
    @Override
    public AuditRepository createAuditRepository() {
        return new AuditRepositoryImpl(connectionProvider);
    }
}
