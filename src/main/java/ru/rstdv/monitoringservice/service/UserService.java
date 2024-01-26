package ru.rstdv.monitoringservice.service;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;


public interface UserService {

    ReadUserDto register(CreateUpdateUserDto createUpdateUserDto);

    ReadUserDto authenticate(String email, String password);
}
