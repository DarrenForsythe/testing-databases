package com.darrenforsythe.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest(excludeAutoConfiguration = TestDatabaseAutoConfiguration.class)
@ActiveProfiles("testcontainers")
@Testcontainers
class UserServiceTestcontainersTest {

    @Container private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>();

    @DynamicPropertySource
    static void setupMySQLDB(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        propertyRegistry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        propertyRegistry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
    }

    private UserService userService;

    @BeforeEach
    void setUp(@Autowired UserRepository userRepository) {
        userService = new UserService(userRepository);
    }

    @Test
    void userShouldSaveWithoutModificationOfNameOrEmployeeId(
            @Autowired UserRepository userRepository) {
        userService.saveUser("darren", "1");

        assertThat(userRepository.findAll())
                .isNotEmpty()
                .hasSize(1)
                .element(0)
                .extracting(User::getEmployeeId, User::getName)
                .containsOnly("1", "darren");
    }

    @Test
    void nameCannotBeNull() {
        assertThatThrownBy(() -> userService.saveUser(null, "1"))
                .hasCauseInstanceOf(ConstraintViolationException.class);
    }

    @RepeatedTest(5)
    void idShouldIncrementCorrectly(
            @Autowired UserRepository userRepository, RepetitionInfo repetitionInfo) {
        userService.saveUser("darren_" + repetitionInfo.getCurrentRepetition(), "1");
        assertThat(
                        userRepository.findById(
                                Integer.toUnsignedLong(repetitionInfo.getCurrentRepetition())))
                .isNotEmpty()
                .get()
                .extracting(User::getName)
                .isEqualTo("darren_" + repetitionInfo.getCurrentRepetition());
    }
}
