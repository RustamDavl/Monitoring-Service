package ru.rstdv.monitoringservice.mapper;

public interface Mapper <F, T>{

    T mapFrom(F from);
}
