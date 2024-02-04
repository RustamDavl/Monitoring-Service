package ru.rstdv.monitoringservice.service;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;

import java.util.List;

/**
 * интерфейс UserService предоставляет операции для взаимодействия с уровнем repository
 *
 * @author RustamD
 * @version 1.0
 */
public interface UserService {

    /**
     * регистрирует пользователя
     *
     * @param createUpdateUserDto - данные пользователя из запроса
     * @return -  сохраненный пользователь с присвоенным идентификатором, возвращаемый пользователю
     */
    ReadUserDto register(CreateUpdateUserDto createUpdateUserDto);

    /**
     * производит аутентификацию пользователя по почте и паролю
     *
     * @param email    - email пользователя
     * @param password - пароль пользователя
     * @return - ReadUserDto если пользователь найден, иначе выбрасывает испольчение UserNotFoundException
     */
    ReadUserDto authenticate(String email, String password);

    /**
     * производит поиск пользователя по идентификатору
     *
     * @param id - идентификатор пользователя
     * @return - ReadUserDto если пользователь найден, иначе выбрасывает испольчение UserNotFoundException
     */
    ReadUserDto findById(Long id);

    /**
     * производит поиск всех пользователей
     *
     * @return - список найденных пользователей
     */
    List<ReadUserDto> findAll();
}
