package ru.rstdv.monitoringservice.service;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;

import java.util.List;


public interface UserService {

    ReadUserDto register(CreateUpdateUserDto createUpdateUserDto);

    ReadUserDto authenticate(String email, String password);

    ReadUserDto findById(Long id);

    List<ReadUserDto> findAll();
}
