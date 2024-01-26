package ru.rstdv.monitoringservice.repository;

import ru.rstdv.monitoringservice.entity.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findById(Long id);
}
