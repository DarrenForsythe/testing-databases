package com.darrenforsythe.repositories;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void userShouldSaveWithoutModificationOfNameOrEmployeeId() {
        userService.saveUser("darren", "1");
        var savedUser = new User();
        savedUser.setName("darren");
        savedUser.setEmployeeId("1");
        verify(userRepository).save(savedUser);
        verifyNoMoreInteractions(userRepository);
    }
}
