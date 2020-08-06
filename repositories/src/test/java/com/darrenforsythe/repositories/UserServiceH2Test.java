package com.darrenforsythe.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DataJpaTest
class UserServiceH2Test {

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
                .extracting(User::getEmployeeId, User::getName, User::getId)
                .containsOnly("1", "darren", 1L);
    }
}
