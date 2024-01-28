package ru.rstdv.monitoringservice.unit.repository;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.Role;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.repository.UserRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest {

    private final UserRepository repository = UserRepositoryImpl.getInstance();

    @AfterEach
    void clearDataBase() {
        UserRepositoryImpl.clearDataBase();
    }

    @Test
    void save() {
        var user1 = repository.save(createUser("1@gmail.com"));
        var user2 = repository.save(createUser("2@gmail.com"));
        var user3 = repository.save(createUser("3@gmail.com"));
        var user4 = repository.save(createUser("4@gmail.com"));
        var user5 = repository.save(createUser("5@gmail.com"));
        var user6 = repository.save(createUser("6@gmail.com"));
        var meterReadings = repository.findAll();

        assertThat(meterReadings).hasSize(6);

        List<Long> ids = meterReadings.stream()
                .map(User::getId)
                .toList();

        assertThat(ids).contains(
                user1.getId(), user2.getId(), user3.getId(),
                user4.getId(), user5.getId(), user6.getId()
        );
    }

    @Test
    void findById_should_return_non_empty_optional() {
        repository.save(createUser("1@gmail.com"));
        repository.save(createUser("2@gmail.com"));
        repository.save(createUser("3@gmail.com"));
        repository.save(createUser("4@gmail.com"));
        repository.save(createUser("5@gmail.com"));
        repository.save(createUser("6@gmail.com"));

        var actualResult = repository.findById(2L);

        var expectedResult = Optional.of(
                User.builder()
                        .id(2L)
                        .firstname("Vi")
                        .email("2@gmail.com")
                        .password("pass")
                        .personalAccount("999999999")
                        .address(
                                Address.builder()
                                        .city("Nigh city")
                                        .street("jig-jig")
                                        .houseNumber("1")
                                        .build()
                        )
                        .role(Role.USER)
                        .build());

        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult.get().getId()).isEqualTo(expectedResult.get().getId());
        assertThat(actualResult.get().getEmail()).isEqualTo(expectedResult.get().getEmail());
        assertThat(actualResult.get().getPersonalAccount()).isEqualTo(expectedResult.get().getPersonalAccount());
        assertThat(actualResult.get().getAddress()).isEqualTo(expectedResult.get().getAddress());

    }

    @Test
    void findById_should_return_empty_optional() {
        repository.save(createUser("1@gmail.com"));
        repository.save(createUser("2@gmail.com"));
        repository.save(createUser("3@gmail.com"));
        repository.save(createUser("4@gmail.com"));
        repository.save(createUser("5@gmail.com"));
        repository.save(createUser("6@gmail.com"));

        var actualResult = repository.findById(7L);


        assertThat(actualResult).isEmpty();


    }

    @Test
    void findAll() {
        repository.save(createUser("1@gmail.com"));
        repository.save(createUser("2@gmail.com"));
        repository.save(createUser("3@gmail.com"));
        repository.save(createUser("4@gmail.com"));
        repository.save(createUser("5@gmail.com"));
        repository.save(createUser("6@gmail.com"));

        var users = repository.findAll();

        assertThat(users).hasSize(6);
    }

    @Test
    void findByEmail_should_return_non_empty_optional() {
        repository.save(createUser("1@gmail.com"));
        repository.save(createUser("2@gmail.com"));
        repository.save(createUser("3@gmail.com"));
        repository.save(createUser("4@gmail.com"));
        repository.save(createUser("5@gmail.com"));
        repository.save(createUser("6@gmail.com"));

        var maybeUser = repository.findByEmail("3@gmail.com");

        assertThat(maybeUser).isNotEmpty();
    }

    @Test
    void findByEmail_should_return_empty_optional() {
        repository.save(createUser("1@gmail.com"));
        repository.save(createUser("2@gmail.com"));
        repository.save(createUser("3@gmail.com"));
        repository.save(createUser("4@gmail.com"));
        repository.save(createUser("5@gmail.com"));
        repository.save(createUser("6@gmail.com"));

        var maybeUser = repository.findByEmailAndPassword("7@gmail.com", "pass");

        assertThat(maybeUser).isEmpty();
    }
    @Test
    void findByEmailAndPassword_should_return_non_empty_optional() {
        repository.save(createUser("1@gmail.com"));
        repository.save(createUser("2@gmail.com"));
        repository.save(createUser("3@gmail.com"));
        repository.save(createUser("4@gmail.com"));
        repository.save(createUser("5@gmail.com"));
        repository.save(createUser("6@gmail.com"));

        var maybeUser = repository.findByEmailAndPassword("5@gmail.com", "pass");

        assertThat(maybeUser).isNotEmpty();
    }


    @Test
    void findByEmailAndPassword_should_return_empty_optional() {
        repository.save(createUser("1@gmail.com"));
        repository.save(createUser("2@gmail.com"));
        repository.save(createUser("3@gmail.com"));
        repository.save(createUser("4@gmail.com"));
        repository.save(createUser("5@gmail.com"));
        repository.save(createUser("6@gmail.com"));

        var maybeUser = repository.findByEmail("7@gmail.com");

        assertThat(maybeUser).isEmpty();
    }



    private User createUser(String email) {
        return User.builder()
                .firstname("Vi")
                .email(email)
                .password("pass")
                .personalAccount("999999999")
                .address(
                        Address.builder()
                                .city("Nigh city")
                                .street("jig-jig")
                                .houseNumber("1")
                                .build()
                )
                .role(Role.USER)
                .build();
    }

}
