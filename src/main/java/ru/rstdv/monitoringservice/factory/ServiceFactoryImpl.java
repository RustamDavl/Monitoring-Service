package ru.rstdv.monitoringservice.factory;


import ru.rstdv.monitoringservice.service.*;

public class ServiceFactoryImpl implements ServiceFactory {
    private RepositoryFactory repositoryFactoryImpl;
    private MapperFactory mapperFactoryImpl;


    public ServiceFactoryImpl() {
        this.repositoryFactoryImpl = new RepositoryFactoryImpl();
        this.mapperFactoryImpl = new MapperFactoryImpl();
    }

    @Override
    public UserService createUserService() {
        return new UserServiceImpl(repositoryFactoryImpl.createUserRepository(), mapperFactoryImpl.createUserMapper(), createAuditService());
    }

    @Override
    public WaterMeterReadingServiceImpl createWaterMeterReadingService() {
        return new WaterMeterReadingServiceImpl(repositoryFactoryImpl.createWaterMeterReadingRepository(),
                repositoryFactoryImpl.createUserRepository(), mapperFactoryImpl.createWaterMeterMapper(), createAuditService());
    }

    @Override
    public ThermalMeterReadingServiceImpl createThermalMeterReadingService() {
        return new ThermalMeterReadingServiceImpl(repositoryFactoryImpl.createThermalMeterReadingRepository(),
                repositoryFactoryImpl.createUserRepository(), mapperFactoryImpl.createThermalMeterMapper(), createAuditService());
    }


    @Override
    public AuditService createAuditService() {
        return new AuditServiceImpl(repositoryFactoryImpl.createAuditRepository(), mapperFactoryImpl.createAuditMapper());
    }
}
