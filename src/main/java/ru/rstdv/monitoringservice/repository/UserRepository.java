package ru.rstdv.monitoringservice.repository;

import ru.rstdv.monitoringservice.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * интерфейс UserRepository предоставляет операции для взаимодействия с базой данных для объекта типа User
 *
 * @author RustamD
 * @version 1.0
 */
public interface UserRepository {

    /**
     * сохраняет пользователя
     *
     * @param user - новый пользователь
     * @return -  сохраненный пользователь с присвоенным идентификатором
     */
    User save(User user);

    /**
     * производит поиск пользователя по почте и паролю
     *
     * @param email    - email пользователя
     * @param password - пароль пользователя
     * @return - Optional.empty() если пользователь не найден, иначе Optional.of(user)
     */
    Optional<User> findByEmailAndPassword(String email, String password);

    /**
     * производит поиск пользователя по идентификатору
     *
     * @param id - идентификатор пользователя
     * @return - Optional.empty() если пользователь не найден, иначе Optional.of(user)
     */
    Optional<User> findById(Long id);

    /**
     * производит поиск всех пользователей
     *
     * @return - список найденных пользователей
     */
    List<User> findAll();

    /**
     * производит поиск пользователя по email
     *
     * @param email - email пользователя
     * @return - Optional.empty() если пользователь не найден, иначе Optional.of(user)
     */
    Optional<User> findByEmail(String email);
}
