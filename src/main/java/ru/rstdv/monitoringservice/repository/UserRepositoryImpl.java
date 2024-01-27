package ru.rstdv.monitoringservice.repository;

import ru.rstdv.monitoringservice.entity.embeddable.Role;
import ru.rstdv.monitoringservice.service.UserService;
import ru.rstdv.monitoringservice.util.DataBaseTable;
import ru.rstdv.monitoringservice.entity.User;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private static final UserRepository INSTANCE = new UserRepositoryImpl();
    private static final DataBaseTable<User> USER_TABLE = new DataBaseTable<>();

    private UserRepositoryImpl() {
    }

    public static UserRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public User save(User user) {
        USER_TABLE.INSERT(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(USER_TABLE.FIND_BY_ID(id));
    }

    @Override
    public List<User> findAll() {
        return USER_TABLE.GET_ALL()
                .stream()
                .filter(user -> user.getRole().name().equals(Role.USER.name()))
                .toList();
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        var maybeUserList = USER_TABLE.GET_ALL()
                .stream()
                .filter(user -> user.getPassword().equals(password) && user.getEmail().equals(email))
                .toList();

        if (maybeUserList.isEmpty())
            return Optional.empty();

        return Optional.ofNullable(maybeUserList.get(0));
    }


}
