package ru.rstdv.monitoringservice.factory;

import ru.rstdv.monitoringservice.mapper.AuditMapper;
import ru.rstdv.monitoringservice.mapper.ThermalMeterMapper;
import ru.rstdv.monitoringservice.mapper.UserMapper;
import ru.rstdv.monitoringservice.mapper.WaterMeterMapper;

public class MapperFactoryImpl implements MapperFactory {
    @Override
    public UserMapper createUserMapper() {
        return UserMapper.INSTANCE;
    }

    @Override
    public ThermalMeterMapper createThermalMeterMapper() {
        return ThermalMeterMapper.INSTANCE;
    }

    @Override
    public WaterMeterMapper createWaterMeterMapper() {
        return WaterMeterMapper.INSTANCE;
    }

    @Override
    public AuditMapper createAuditMapper() {
        return AuditMapper.INSTANCE;
    }
}
