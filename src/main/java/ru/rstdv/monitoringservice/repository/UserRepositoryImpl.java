package ru.rstdv.monitoringservice.repository;

import lombok.RequiredArgsConstructor;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.Role;
import ru.rstdv.monitoringservice.util.ConnectionProvider;
import ru.rstdv.monitoringservice.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final ConnectionProvider connectionProvider;
    private static final String SAVE_SQL = """
            INSERT INTO users(firstname, email, password, personal_account, role, city, street, house_number)\s
            VALUES (?, ?, ?, ?, ?, ?, ?, ?);
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, firstname, email, personal_account, role, city, street, house_number
            FROM users
            WHERE id = ?
            """;
    private static final String FIND_BY_EMAIL_SQL = """
            SELECT id, firstname, email, personal_account, role, city, street, house_number
            FROM users
            WHERE email = ?
            """;
    private static final String FIND_BY_EMAIL_AND_PASSWORD_SQL = """
            SELECT id, firstname, email, personal_account, role, city, street, house_number
            FROM users
            WHERE email = ? AND password = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id, firstname, email, personal_account, role, city, street, house_number
            FROM users
            """;

    private static final String DELETE_ALL_SQL = """
            DELETE FROM users;
            """;

    @Override
    public User save(User user) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getPersonalAccount());
            preparedStatement.setString(5, user.getRole().name());
            preparedStatement.setString(6, user.getAddress().getCity());
            preparedStatement.setString(7, user.getAddress().getStreet());
            preparedStatement.setString(8, user.getAddress().getHouseNumber());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong("id"));
            }
            return user;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<User> findById(Long id) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(User.builder()
                        .id(resultSet.getLong("id"))
                        .firstname(resultSet.getString("firstname"))
                        .email(resultSet.getString("email"))
                        .personalAccount(resultSet.getString("personal_account"))
                        .role(Role.valueOf(resultSet.getString("role")))
                        .address(Address.builder()
                                .city(resultSet.getString("city"))
                                .street(resultSet.getString("street"))
                                .houseNumber(resultSet.getString("house_number"))
                                .build())
                        .build()
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(User.builder()
                        .id(resultSet.getLong("id"))
                        .firstname(resultSet.getString("firstname"))
                        .email(resultSet.getString("email"))
                        .personalAccount(resultSet.getString("personal_account"))
                        .role(Role.valueOf(resultSet.getString("role")))
                        .address(Address.builder()
                                .city(resultSet.getString("city"))
                                .street(resultSet.getString("street"))
                                .houseNumber(resultSet.getString("house_number"))
                                .build())
                        .build()
                );
            }
            return users;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_SQL)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(User.builder()
                        .id(resultSet.getLong("id"))
                        .firstname(resultSet.getString("firstname"))
                        .email(resultSet.getString("email"))
                        .personalAccount(resultSet.getString("personal_account"))
                        .role(Role.valueOf(resultSet.getString("role")))
                        .address(Address.builder()
                                .city(resultSet.getString("city"))
                                .street(resultSet.getString("street"))
                                .houseNumber(resultSet.getString("house_number"))
                                .build())
                        .build()
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_AND_PASSWORD_SQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(User.builder()
                        .id(resultSet.getLong("id"))
                        .firstname(resultSet.getString("firstname"))
                        .email(resultSet.getString("email"))
                        .personalAccount(resultSet.getString("personal_account"))
                        .role(Role.valueOf(resultSet.getString("role")))
                        .address(Address.builder()
                                .city(resultSet.getString("city"))
                                .street(resultSet.getString("street"))
                                .houseNumber(resultSet.getString("house_number"))
                                .build())
                        .build()
                );

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void deleteAll() {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL_SQL)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
