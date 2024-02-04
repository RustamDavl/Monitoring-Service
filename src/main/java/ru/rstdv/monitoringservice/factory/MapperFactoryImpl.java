package ru.rstdv.monitoringservice.factory;

import ru.rstdv.monitoringservice.mapper.*;

public class MapperFactoryImpl implements MapperFactory {
    @Override
    public UserMapper createUserMapper() {
        return new UserMapperImpl();
    }
    @Override
    public ThermalMeterMapper createThermalMeterMapper() {
        return new ThermalMeterMapperImpl();
    }
    @Override
    public WaterMeterMapper createWaterMeterMapper() {
        return new WaterMeterMapperImpl();
    }
    @Override
    public AuditMapper createAuditMapper() {
        return new AuditMapperImpl();
    }
}
