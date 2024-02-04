package ru.rstdv.monitoringservice.intergration.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.Role;
import ru.rstdv.monitoringservice.util.IntegrationTestBase;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.repository.UserRepositoryImpl;
import ru.rstdv.monitoringservice.util.LiquibaseUtil;
import ru.rstdv.monitoringservice.util.TestConnectionProvider;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;


public class UserRepositoryIT extends IntegrationTestBase {
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        TestConnectionProvider testConnectionProvider = new TestConnectionProvider(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseUtil.start(testConnectionProvider);
        userRepository = new UserRepositoryImpl(testConnectionProvider);
    }

    @AfterEach
    void clear() {
        LiquibaseUtil.dropAll();
    }

    @Test
    void save() {
        var user1 = userRepository.save(createUser("7@gmail.com"));
        var user2 = userRepository.save(createUser("8@gmail.com"));
        var user3 = userRepository.save(createUser("9@gmail.com"));
        var user4 = userRepository.save(createUser("10@gmail.com"));
        var user5 = userRepository.save(createUser("11@gmail.com"));

        var usersAmount = userRepository.findAll().size();
        var users = userRepository.findAll();

        Assertions.assertThat(users).hasSize(usersAmount);

        List<Long> ids = users.stream()
                .map(User::getId)
                .toList();
        assertThat(ids).contains(
                user1.getId(), user2.getId(),
                user3.getId(), user4.getId(),
                user5.getId()
        );

    }

    @Test
    void findById_should_return_non_empty_optional() {

        var actualResult = userRepository.findById(2L);

        var expectedResult = Optional.of(
                User.builder()
                        .id(2L)
                        .firstname("Vi")
                        .email("user2@gmail.com")
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

        var actualResult = userRepository.findById(25L);
        assertThat(actualResult).isEmpty();


    }

    @Test
    void findAll() {
        var users = userRepository.findAll();
        assertThat(users).hasSize(6);
    }

    @Test
    void findByEmail_should_return_non_empty_optional() {
        var maybeUser = userRepository.findByEmail("user3@gmail.com");
        assertThat(maybeUser).isNotEmpty();
    }

    @Test
    void findByEmail_should_return_empty_optional() {
        var maybeUser = userRepository.findByEmailAndPassword("user11@gmail.com", "pass");
        assertThat(maybeUser).isEmpty();
    }

    @Test
    void findByEmailAndPassword_should_return_non_empty_optional() {
        var maybeUser = userRepository.findByEmailAndPassword("user5@gmail.com", "pass");
        assertThat(maybeUser).isNotEmpty();
    }


    @Test
    void findByEmailAndPassword_should_return_empty_optional() {
        var maybeUser = userRepository.findByEmailAndPassword("user11@gmail.com", "pass");
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
